package com.example.newsdemoapplication.ui.test

import android.app.Service
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.Util
import com.example.newsdemoapplication.adapter.ListDragAdapter
import com.example.newsdemoapplication.adapter.ListDragAdapter.MyDragViewHolder
import com.example.newsdemoapplication.adapter.NewsContentAdapter
import com.example.newsdemoapplication.callback.ItemDragHelperCallBack
import com.example.newsdemoapplication.callback.OnItemClickListener
import com.example.newsdemoapplication.databinding.TestFragmentBinding
import com.example.newsdemoapplication.model.test.NewsDatabase
import com.example.newsdemoapplication.popup.ChapterPopupView
import com.example.newsdemoapplication.ui.dashboard.ItemHelper
import com.example.newsdemoapplication.util.CommonUtils
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.view.DragRecycleView.DragAndPressCallBack
import com.example.newsdemoapplication.vo.ChapterVo
import com.example.newsdemoapplication.vo.ContentVo
import com.example.newsdemoapplication.vo.NewsVo
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gzp.baselib.base.MvvmBaseFragment
import com.gzp.baselib.constant.Constants
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

private val Int.rad: Int
    get() {
       return Math.round(Math.random()*this).toInt()
    }

class TestFragment : MvvmBaseFragment<TestViewModel, TestFragmentBinding>(),CoroutineScope by MainScope() {
    companion object {
        fun newInstance() = TestFragment()
    }
    override fun getLayoutResId()=R.layout.test_fragment
    private var editChapter: Boolean=false
    var chapterDragView:ChapterDragView?=null
    override fun onResume() {
        super.onResume()
        arguments?.getSerializable(Constants.ChapterVo)?.let {
            saveChapter(it as ChapterVo)
        }
    }

    private fun saveChapter(chapterVo: ChapterVo) {
        vm.listChapter.apply {
            var find =false
            var index=-1
            value?.forEachIndexed { i ,it->
                if(it.id==chapterVo.id) index =i }
            val newlist =if(index<0)value else {value?.removeAt(index)
                value
            }

            newlist?.also {
                val index =if(chapterVo.index<0) 0 else if(chapterVo.index>=newlist.size) newlist.size else chapterVo.index
                if(chapterVo.index!=index){ chapterVo.index=index }
                it.add(chapterVo.index,chapterVo)
            }?.let {
                Toast.makeText(context, "${if(find)"更新" else "新增"} 了-《${chapterVo.chapterName}》", Toast.LENGTH_SHORT).show()
                postValue(it)
            }
        }
    }

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.btnCloseRight.apply {
            setOnClickListener { showLoading() }
        }
        initFrameLayout()

        initTitleRecycleView()
        leftDrawerPopupView
        chapterDragView?.parent=leftDrawerPopupView
        mContentRecycleView.invalidate()
        initObserverbal()
        loadData()
        launch {
            delay(5000)
        }
    }

    private fun loadData() {
        val list= mutableListOf<ChapterVo>()
        Toast.makeText(context, "初始化生成了一些7 条假数据", Toast.LENGTH_SHORT).show()
        for (i in 0..7) {
            list.add(ChapterVo("章节$i",locked = radom(),index = i).apply {
                val l = mutableListOf<NewsVo>()
                for(j in 0.. 20.rad){
                    l.add(NewsVo("随机$j").apply {
                        val ll = mutableListOf<ContentVo>()
                        for(m in 0.. 20.rad){
                            ll.add(ContentVo("随机$m","sss"))
                        }
                        listContent=ll.toList()
                    })
                }
                listNews=l.toList()
            })
        }
        vm.listChapter.postValue(list)
//        }
    }

    private fun radom()=
        Math.random()>0.5

    private fun initObserverbal() {
        vm.apply {
            currChapter.observe(this@TestFragment){
                binding.frameLayout.apply {
                    suoTextViewId.text=it.getLockStr()
                    ddTextViewId.text=it.chapterName
                    txTextViewId.text=it.description
                }
                titleAdapter.setmDatas(it.listNews?.map { it.title })
                mContentAdapter.setData(it.listNews)
            }
        }
    }

    private fun initFrameLayout() {
        binding.frameLayout.apply {
            cyImagebuttonId.setOnClickListener {//左侧弹窗按钮
//                binding.drawerLayout.openDrawer(binding.linearLayout1Id)
                leftPopup?.let {
                    it.show()
                }?:XPopup.Builder(context)
                        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                        .popupPosition(PopupPosition.Left)//左边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .autoDismiss(true)
                        .dismissOnBackPressed(true)
                        .dismissOnTouchOutside(true)
                        .asCustom(leftDrawerPopupView)
                        .show().also {
                            leftPopup = it
                        }
            }
            szImagebuttonId.setOnClickListener{navigationEdit()}
        }
        binding.leftNavView.inflateHeaderView(R.layout.nav_header_main).apply {
            findViewById<TextView>(R.id.textView).apply {
                setOnClickListener {   binding.drawerLayout.closeDrawer(binding.leftNavView) }
            }
        }
        binding.drawerLayout.setDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                if(binding.drawerLayout.isDrawerOpen(binding.leftNavView)){
                    binding.drawerLayout.closeDrawer(binding.leftNavView)
                    leftDrawerPopupView.show()
                }
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

        })
    }

    private fun navigationEdit() {
        try {
            leftDrawerPopupView.dismiss()
        }catch (e:Exception){
            Log.e("TAG", "navigationEdit:$e ", )
        }
        NavHostFragment.findNavController(this@TestFragment).navigate(R.id.action_navigation_test_to_navigation_add_section, Bundle().apply {
            putBoolean(Constants.IsEdit, true)
            Log.e("TAG", "initFrameLayout: true", )
            putSerializable(Constants.ChapterVo, vm.currChapter.value)
        })
    }

    private val newsDatabase by lazy {
        NewsDatabase.getInstance(requireContext())
    }
//    private val newsDao by lazy {
//        newsDatabase.getNewsDao()
//    }
    private var leftPopup: BasePopupView?=null
    private val SINGLE_LINE = 0
    private val HALF_EXPANDED = 1
    private val COMPLETE_EXPANDED = 2

    val ColumnNum = 4


    //顶部 抽屉展开按钮
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
    //收起左侧弹窗按钮
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
               //设置滚动监听 当使显示的第一个item 是标题栏的第一个
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
            if(editChapter){
                editChapter=false
                navigationEdit()
            }else
            XPopup.Builder(activity).asInputConfirm("重命名", "请输入内容。") { text: String? ->
                    list.set(currSelectedPosition, text)
                    titleAdapter.notifyItemChanged(currSelectedPosition)
                    mContentAdapter.notifyItemChanged(currSelectedPosition)
            } .show()
        }
        findViewById<View>(R.id.tv_delete)?.setOnClickListener { v: View? ->
            if(editChapter){
                editChapter=false
                vm.listChapter.apply {
                    val index =value?.indexOf(vm.currChapter.value)
                    val list =value?.also { it.removeAt(index?:0) }
                    postValue(list)
                }
            }else{
                Log.e("TAG", "initBottomSheetDiaLog: $currSelectedPosition")
                list.removeAt(currSelectedPosition)
                titleAdapter.notifyItemRemoved(currSelectedPosition)
                mContentAdapter.notifyItemRemoved(currSelectedPosition)
            }
            dismiss()

        }
    } }
    //左侧弹窗列表
    private val leftDrawerPopupView by lazy {
        ChapterPopupView(requireContext()).apply {
            llContainer.apply {
                chapterDragView=ChapterDragView(context,vm.listChapter).apply {
                    layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
                    setBackgroundColor(Color.parseColor("#234567"))
                    clickCallBack=object :ChapterDragView.ClickCallBack{
                        override fun onChapterClicked(chapterVo: ChapterVo) {
                           vm.currChapter.postValue(chapterVo)
                        }
                    }
                    mCallBack=object: DragAndPressCallBack {
                            //设置移动监听回调
                            override fun onLongPress() {
                                editChapter =true
                                bottomSheetDialog.show()
                            }
                            override fun onAfterPressMove() {
                                longPress = false
                                bottomSheetDialog.dismiss()
                            }
                        }
                }.also { addView(it) }
            }
            mOnClickListener= View.OnClickListener {
                NavHostFragment.findNavController(this@TestFragment)
                        .navigate(R.id.action_navigation_test_to_navigation_add_section
                                , Bundle().apply {
                            putBoolean(Constants.IsEdit, false)
                            }
                        )
            }
        }
    }

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
         Util.getData(17)
    private val mContentAdapter by lazy {
        NewsContentAdapter(context, listOf())
    }
    private var longPress = false



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
                    longPress=false
                }
            }

            override fun itemSelected(position: Int) { //选中title 后的响应操作
                if (position < 0) return
                mTitleRecycleView.count = 2
                if (position != currSelectedPosition) {
                    (mTitleRecycleView.findViewHolderForAdapterPosition(currSelectedPosition) as? MyDragViewHolder)?.setSelected(false)
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
        val layoutParams = mTitleRecycleView.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = titleMaxHeight
        mTitleRecycleView.layoutParams = layoutParams
        showExpandFoldBtns()
    }

    private fun showCompleteExpand() {
        currTitleState = COMPLETE_EXPANDED
        val layoutParams = mTitleRecycleView.layoutParams as LinearLayout.LayoutParams
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