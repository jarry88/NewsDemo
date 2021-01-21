package com.example.newsdemoapplication.ui.home

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.Util
import com.example.newsdemoapplication.adapter.ArticleListAdapter
import com.example.newsdemoapplication.adapter.ListDragAdapter
import com.example.newsdemoapplication.adapter.ListSubsectionDragAdapter
import com.example.newsdemoapplication.callback.ItemDragHelperCallBack
import com.example.newsdemoapplication.callback.OnItemClickListener
import com.example.newsdemoapplication.databinding.BottonDialogBinding
import com.example.newsdemoapplication.databinding.ChapterDetailViewBinding
import com.example.newsdemoapplication.dsl.addOnClick
import com.example.newsdemoapplication.dsl.observe
import com.example.newsdemoapplication.dsl.wrap_content
import com.example.newsdemoapplication.ui.TitleState.*
import com.example.newsdemoapplication.ui.dashboard.ItemHelper
import com.example.newsdemoapplication.util.CommonUtils
import com.example.newsdemoapplication.util.log
import com.example.newsdemoapplication.util.toast
import com.example.newsdemoapplication.view.DragRecycleView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gzp.baselib.utils.DensityUtil
import com.lxj.xpopup.XPopup
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@SuppressLint("ViewConstructor")
class ChapterDetailFragment( val parent:HomeV2Fragment):Fragment() {
    val binding:ChapterDetailViewBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chapter_detail_view,null,false)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }
    val columnNum = 4
    var currSelectedTitlePosition=0
    private var longPress = false
    val titleMaxHeight by lazy {DensityUtil.getScreenHeight(requireContext())*1/3  }
    var currTitleState= SingleLine
        set(value)  {
            if(field!=value){
                field=value
                showExpandFoldButtons()
            }
        }
    var supportHalfExpand =false
    private val viewModel:ChapterDetailViewModel by viewModel()

    var list =viewModel.currSubsectionList.value?:listOf()
    private val titleLayoutManager by lazy {
        GridLayoutManager(context, columnNum * 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int)= list.let{
                    val end: Int = it.size % columnNum
                    if (it.size - position <= end) columnNum * 3 / end else 3
                }
            }
        }
    }
    val titleAdapter by lazy {
        ListSubsectionDragAdapter(requireContext(), list).apply {
            setOnItemClickListener(
                    object : OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            currSelectPosition = position
                            contentScrollToPosition(position)
                        }
                        override fun onItemLongClick(view: View, position: Int) {}
                    })
        }
    }
    private val mContentAdapter by lazy {
        ArticleListAdapter(context, mutableListOf())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
        with(binding.rvTitle){
            layoutManager = titleLayoutManager
            adapter = titleAdapter
            postDelayed({
            //视图刷新后，计算视图高度
            if (measuredHeight > titleMaxHeight) {
                supportHalfExpand = true
                showHalfExpand()
            } else {
                currTitleState =CompleteExpand
            }
        }, 50)
        }
        binding.btnAddSubsection.addOnClick {
            viewModel.insertSubsection(viewModel.factorySubsection(parent.selectedChapterId).toast)
            viewModel.getSubsectionListByChapterId(parent.selectedChapterId.log)
        }
        binding.btnFold.addOnClick{viewModel.deleteAllSubsection().toast("清理完毕")}

        binding.rvTitle.observe(viewModel.currSubsectionList){
            list=it.toast
            titleAdapter.setmDatas(list)
        }
        binding.rvContentList.adapter=mContentAdapter
        initTitleRecycleView()
    }
    /**
     * 根据标题栏折叠状态显示按钮
     */
    private fun showExpandFoldButtons() {
        with(binding){
            when (currTitleState) {
                SingleLine -> {
                    btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                    btnFold.visibility = View.GONE
                }
                HalfExpand -> {
                    btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                    btnFold.visibility = View.VISIBLE
                }
                CompleteExpand -> {
                    btnExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
                    btnFold.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun showSingLine() = with(binding.rvTitle){
            val lp = layoutParams as LinearLayout.LayoutParams
            lp.height =  CommonUtils.dp2px(context, 40f)
            layoutParams=   lp
            currTitleState = SingleLine
        }
    private fun showHalfExpand() = with(binding.rvTitle){
            val lp = layoutParams as LinearLayout.LayoutParams
            lp.height = titleMaxHeight
            layoutParams=lp
            currTitleState = HalfExpand
        }
    private fun showCompleteExpand() = with(binding.rvTitle){
            val lp = layoutParams as LinearLayout.LayoutParams
            lp.height = wrap_content
            layoutParams=lp
            currTitleState = CompleteExpand
        }

    private fun initTitleRecycleView() {
        val itemTouchHelper = ItemTouchHelper(ItemDragHelperCallBack(object : ItemHelper {
            override fun itemMoved(oldPosition: Int, newPosition: Int) {
                "move $oldPosition to $newPosition".log
                //交换变换位置的集合数据
                Collections.swap(titleAdapter.getData(), oldPosition, newPosition)
                titleAdapter.notifyItemMoved(oldPosition, newPosition)
//                viewModel.currSelectSubsection=
                currSelectedTitlePosition = newPosition
            }

            override fun itemDismiss(position: Int) {}
            override fun itemClear(position: Int) {
                currSelectedTitlePosition = position
                titleAdapter.currSelectPosition = currSelectedTitlePosition
                titleAdapter.notifyDataSetChanged()
                if (longPress) {
                    bottomSheetDialog.show()
                    longPress = false
                }
            }

            override fun itemSelected(position: Int) { //选中title 后的响应操作
                if (position < 0) return
                binding.rvTitle.count = 2
                if (position != currSelectedTitlePosition) {
                    (binding.rvTitle.findViewHolderForAdapterPosition(currSelectedTitlePosition) as? ListDragAdapter.MyDragViewHolder)?.setSelected(false)
                    currSelectedTitlePosition = position
                }
                val vib = context!!.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(150)
                //                 记录弹窗信息
                longPress = true
            }
        }))
        itemTouchHelper.attachToRecyclerView(binding.rvTitle)
        binding.rvTitle.mCallBack = object : DragRecycleView.DragAndPressCallBack {
            //设置移动监听回调
            override fun onLongPress() {
                bottomSheetDialog.show()
                binding.rvTitle.bottomShowTime = System.currentTimeMillis()
            }

            override fun onAfterPressMove() {
                longPress = false
                bottomSheetDialog.dismiss()
            }
        }
//        mTitleRecycleView.postDelayed({
//            //视图刷新后，计算视图高度
//            Util.Loge(String.format("height %d", mTitleRecycleView.measuredHeight))
//            if (mTitleRecycleView.measuredHeight > titleMaxHeight) {
//                supportHalfExpand = true
//                showHalfExpand()
//            } else {
//                currTitleState = COMPLETE_EXPANDED
//            }
//        }, 50)
    }
    fun updateChapterId(id:Long){
        viewModel.getSubsectionListByChapterId(id)
    }
    private fun contentScrollToPosition(position: Int) {
        val mLayoutManager = binding.rvContentList.layoutManager as LinearLayoutManager
        mLayoutManager.scrollToPositionWithOffset(position, 0)
    }

    val bottomSheetDialog by lazy { BottomSheetDialog(context).apply {
        val binding:BottonDialogBinding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.botton_dialog,null,false)
        setContentView(binding.root)
        setCancelable(true)
        binding.tvCancel.addOnClick { dismiss() }
        binding.tvDelete.addOnClick {             "delete".toast
        }
        binding.tvRename.addOnClick {
            "rename".toast
        }
//        findViewById<View>(R.id.tv_cancel)?.setOnClickListener {dismiss() }
//        findViewById<View>(R.id.tv_rename)?.setOnClickListener {
//            Log.e("TAG", "initBottomSheetDiaLog: $currSelectedPosition")
//            dismiss()
//            if(editChapter){
//                editChapter=false
//                navigationEdit()
//            }else
//                XPopup.Builder(activity).asInputConfirm("重命名", "请输入内容。") { text: String? ->
//                    list[currSelectedPosition] = text
//                    titleAdapter.notifyItemChanged(currSelectedPosition)
//                    mContentAdapter.notifyItemChanged(currSelectedPosition)
//                }.show()
//        }
//        findViewById<View>(R.id.tv_delete)?.setOnClickListener { v: View? ->
//            if(editChapter){
//                editChapter=false
//                vm.listChapter.apply {
//                    postValue(value?.filterNot { it==vm.currChapter.value }?.toList())
//                }
//            }else{
//                Log.e("TAG", "initBottomSheetDiaLog: $currSelectedPosition")
//                list.removeAt(currSelectedPosition)
//                titleAdapter.notifyItemRemoved(currSelectedPosition)
//                mContentAdapter.notifyItemRemoved(currSelectedPosition)
//            }
//            dismiss()
//
//        }
    } }

}