package com.example.newsdemoapplication.ui.test

import android.app.Service
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdemoapplication.NewsVo
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.Util
import com.example.newsdemoapplication.adapter.ListDragAdapter
import com.example.newsdemoapplication.adapter.ListDragAdapter.MyDragViewHolder
import com.example.newsdemoapplication.adapter.NewsContentAdapter
import com.example.newsdemoapplication.callback.ItemDragHelperCallBack
import com.example.newsdemoapplication.callback.OnItemClickListener
import com.example.newsdemoapplication.databinding.TestFragmentBinding
import com.example.newsdemoapplication.popup.LeftDrawerPopupView
import com.example.newsdemoapplication.popup.ListDrawerPopupView
import com.example.newsdemoapplication.ui.dashboard.ItemHelper
import com.example.newsdemoapplication.util.CommonUtils
import com.example.newsdemoapplication.view.DragRecycleView.DragAndPressCallBack
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gzp.baselib.base.MvvmBaseFragment
import com.gzp.baselib.constant.Constants
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import java.util.*

class TestFragment : MvvmBaseFragment<TestViewModel, TestFragmentBinding>() {
    companion object {
        fun newInstance() = TestFragment()
    }

    private var leftPopup: BasePopupView?=null
    private val SINGLE_LINE = 0
    private val HALF_EXPANDED = 1
    private val COMPLETE_EXPANDED = 2

    val ColumnNum = 4

    val btnExpand by lazy { binding.frameLayout.frameLayout.btnExpand.apply {
        setOnClickListener {
            if (currTitleState == COMPLETE_EXPANDED) {
                if (supportHalfExpand) showHalfExpand() else showSingLine()
            } else if (currTitleState == HALF_EXPANDED) {
                showCompleteExpand()
            } else {
                if (supportHalfExpand) showHalfExpand() else showCompleteExpand()
            }
        }
    } }
    private val mTitleRecycleView by lazy {
        binding.frameLayout.frameLayout.rvTitle.apply {
            layoutManager = titleLayoutManager
            adapter = titleAdapter
        }

    }
    val btnFold by lazy { binding.frameLayout.frameLayout.btnFold.apply { setOnClickListener { showSingLine() } } }


    private val titleLayoutManager by lazy {
        GridLayoutManager(requireContext(), ColumnNum * 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val end: Int = list.size % ColumnNum
                    return if (list.size - position <= end) ColumnNum * 3 / end else 3
                }
            }
        }
    }
    val titleAdapter by lazy {
        ListDragAdapter(activity, list).apply {
            setOnItemClickListener(
                object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        currSelectedPosition = position
                        currSelectPosition = position
                        contentScrollToPosition(position)
                    }

                    override fun onItemLongClick(view: View, position: Int) {}
                })
        }
    }
    private val mContentRecycleView by lazy {
        binding.frameLayout.frameLayout.rvContentList.apply {
            adapter = mContentAdapter
           setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
               val linearLayoutManager = layoutManager as LinearLayoutManager
               val topPosition = linearLayoutManager.findFirstVisibleItemPosition()
               if (topPosition >= 0 && topPosition != currSelectedPosition) {
                   currSelectedPosition = topPosition
                   titleAdapter.currSelectPosition = currSelectedPosition
                   Log.e("TAG", "initContentRecycleView: $topPosition")
                   titleLayoutManager.scrollToPositionWithOffset(topPosition, 0)
               }
           }
        }
    }
    val bottomSheetDialog by lazy { BottomSheetDialog(requireContext()).apply {
        setContentView(R.layout.botton_dialog)
        setCancelable(true)
        findViewById<View>(R.id.tv_cancel)?.setOnClickListener {dismiss() }
        findViewById<View>(R.id.tv_rename)?.setOnClickListener {
            Log.e("TAG", "initBottomSheetDiaLog: $currSelectedPosition")
            dismiss()
            XPopup.Builder(context).asInputConfirm("重命名", "请输入内容。"
            ) { text: String? ->
                list.set(currSelectedPosition, text)
                titleAdapter.notifyItemChanged(currSelectedPosition)
                mContentAdapter.notifyItemChanged(currSelectedPosition)
            }
                    .show()
        }
        findViewById<View>(R.id.tv_delete)?.setOnClickListener { v: View? ->
            Log.e("TAG", "initBottomSheetDiaLog: $currSelectedPosition")
            list.removeAt(currSelectedPosition)
            titleAdapter.notifyItemRemoved(currSelectedPosition)
            mContentAdapter.notifyItemRemoved(currSelectedPosition)
            dismiss()
        }
    } }

    //标题栏是否支持局部展开
    private var supportHalfExpand = false
    private var currSelectedPosition = 0
    private var titleMaxHeight //像素
            = CommonUtils.dp2px(activity, 200f)
    private val singleLineHeight by lazy {
        CommonUtils.dp2px(activity, 40f)
    }


    private var currTitleState = 0
    var mData: List<NewsVo> = ArrayList()
    val list=
         Util.getData(47)
    private val mContentAdapter by lazy {
        NewsContentAdapter(context, list)
    }
    private var longPress = false
    private val mList by lazy {
        Util.getData(47)
    }
    private val leftAdapter by lazy {
        ListDragAdapter(context, mList)
    }
    override fun getLayoutResId()=R.layout.test_fragment

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.btnCloseRight.apply {
            setOnClickListener { showLoading() }
        }
        binding.frameLayout.apply {
            cyImagebuttonId.setOnClickListener {
//                binding.drawerLayout.openDrawer(binding.linearLayout1Id)
                leftPopup?.let {
                    it.show()
                }?:XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                        .popupPosition(PopupPosition.Left)//左边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(LeftDrawerPopupView(requireContext()))
                        .show().also {
                            leftPopup = it
                        }
            }
            szImagebuttonId.setOnClickListener{
//                binding.drawerLayout.openDrawer(binding.leftNavView)
                XPopup.Builder(context)
                        .dismissOnTouchOutside(false)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(ListDrawerPopupView(requireContext()))
                        .show();
            }
        }
        binding.leftNavView.inflateHeaderView(R.layout.nav_header_main).apply {
            findViewById<TextView>(R.id.textView).apply {
                setOnClickListener {   binding.drawerLayout.closeDrawer(binding.leftNavView) }
            }
        }
        binding.btnClose.setOnClickListener {
            it.setOnClickListener { binding.drawerLayout.closeDrawer(binding.leftNavView) }
        }
        binding.btnAdd.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_test_to_navigation_add_section, Bundle().apply {
                putBoolean(Constants.IsEdit, false)
            })
        }
        initLeftRv()
        initTitleRecycleView()
        mContentRecycleView.invalidate()
    }
    fun initLeftRv(){
        binding.rvLeft.adapter=
                leftAdapter.apply {
                    object :OnItemClickListener{
                        override fun onItemClick(view: View?, position: Int) {
                            currSelectPosition = position
                        }
                        override fun onItemLongClick(view: View?, position: Int) {
//                            TODO("Not yet implemented")
                        }
                    }
        }
        binding.rvLeft.setOnClickListener {
            Log.e("TAG", "initLeftRv: ")
        }
    }


    private fun initTitleRecycleView() {

        val itemTouchHelper = ItemTouchHelper(ItemDragHelperCallBack(object : ItemHelper {
            override fun itemMoved(oldPosition: Int, newPosition: Int) {
                Util.Loge("move")
                //交换变换位置的集合数据
                Collections.swap(titleAdapter.getData(), oldPosition, newPosition)
                titleAdapter.notifyItemMoved(oldPosition, newPosition)
                currSelectedPosition = newPosition
            }

            override fun itemDismiss(position: Int) {}
            override fun itemClear(position: Int) {
                currSelectedPosition = position
                titleAdapter.currSelectPosition = currSelectedPosition
                titleAdapter.notifyDataSetChanged()
                if (longPress) {
                    bottomSheetDialog.show()
                }
                //                }
            }

            override fun itemSelected(position: Int) { //选中title 后的响应操作
                if (position < 0) return
                mTitleRecycleView.count = 1
                if (position != currSelectedPosition) {
                    (mTitleRecycleView.findViewHolderForAdapterPosition(currSelectedPosition) as MyDragViewHolder).setSelected(false)
                    currSelectedPosition = position
                }
                val vib = context!!.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(150)
                //                 记录弹窗信息
                longPress = true
            }
        }))
        itemTouchHelper.attachToRecyclerView(mTitleRecycleView)
        mTitleRecycleView.mCallBack = object : DragAndPressCallBack {
            //设置移动监听回调
            override fun onLongPress() {
                bottomSheetDialog.show()
                mTitleRecycleView.bottomShowTime = System.currentTimeMillis()
            }

            override fun onAfterPressMove() {
                longPress = false
                bottomSheetDialog.dismiss()
            }
        }
        mTitleRecycleView.postDelayed({
            //视图刷新后，计算视图高度
            Util.Loge(String.format("height %d", mTitleRecycleView.measuredHeight))
            if (mTitleRecycleView.measuredHeight > titleMaxHeight) {
                supportHalfExpand = true
                showHalfExpand()
            } else {
                currTitleState = COMPLETE_EXPANDED
            }
        }, 50)
    }

    private fun contentScrollToPosition(position: Int) {
        val mLayoutManager = mContentRecycleView.layoutManager as LinearLayoutManager
        mLayoutManager.scrollToPositionWithOffset(position, 0)
//        mContentRecycleView.offsetTopAndBottom(0);
    }

    private fun showSingLine() {
        currTitleState = SINGLE_LINE
        val layoutParams = mTitleRecycleView.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = singleLineHeight
        mTitleRecycleView.layoutParams = layoutParams
        showExpandFoldBtns()
    }

    private fun showHalfExpand() {
        currTitleState = HALF_EXPANDED
        val layoutParams = mTitleRecycleView.getLayoutParams() as LinearLayout.LayoutParams
        layoutParams.height = titleMaxHeight
        mTitleRecycleView.layoutParams = layoutParams
        showExpandFoldBtns()
    }

    private fun showCompleteExpand() {
        currTitleState = COMPLETE_EXPANDED
        val layoutParams = mTitleRecycleView.getLayoutParams() as LinearLayout.LayoutParams
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        mTitleRecycleView.layoutParams = layoutParams
        showExpandFoldBtns()
    }

    /**
     * 根据标题栏折叠状态显示按钮
     */
    private fun showExpandFoldBtns() {
        when (currTitleState) {
            SINGLE_LINE -> {
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                btnFold.visibility = View.GONE
            }
            HALF_EXPANDED -> {
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                btnFold.visibility = View.VISIBLE
            }
            COMPLETE_EXPANDED -> {
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
                btnFold.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }



}