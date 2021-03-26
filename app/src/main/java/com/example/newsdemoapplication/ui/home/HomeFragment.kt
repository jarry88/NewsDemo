package com.example.newsdemoapplication.ui.home

import android.annotation.SuppressLint
import android.app.Service
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdemoapplication.Constants
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.HomeFragmentBinding
import com.example.newsdemoapplication.view.ListDragAdapter
import com.example.newsdemoapplication.view.ListDragAdapter.MyDragViewHolder
import com.example.newsdemoapplication.view.NewsContentAdapter
import com.example.newsdemoapplication.util.callback.ItemHelper
import com.example.newsdemoapplication.util.CommonUtils
import com.example.newsdemoapplication.util.callback.ItemDragHelperCallBack
import com.example.newsdemoapplication.util.callback.OnItemClickListener
import com.example.newsdemoapplication.util.rad
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.view.TitleRecycleView.DragAndPressCallBack
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.model.ContentVo
import com.example.newsdemoapplication.model.NewsVo
import com.example.newsdemoapplication.util.common.MvvmBaseFragment
import com.example.newsdemoapplication.util.getRandomData
import com.example.newsdemoapplication.util.log
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.XPopupCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * app首页
 * @author gzp
 */
class HomeFragment : MvvmBaseFragment<HomeViewModel, HomeFragmentBinding>(),CoroutineScope by MainScope() {
    private val TAG =this::class.java.simpleName
    override fun getLayoutResId()=R.layout.home_fragment
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
            var index=-1
            value?.forEachIndexed { i, it->
                if(it.id==chapterVo.id) index =i }
            val newlist =if(index<0)value else {value?.removeAt(index)
                value
            }
            newlist?.also {
                val chapterVoIndex =if(chapterVo.index<0) 0 else if(chapterVo.index>=newlist.size) newlist.size else chapterVo.index
                if(chapterVo.index!=index){ chapterVo.index=chapterVoIndex }
                it.add(chapterVo.index, chapterVo)
            }?.let {
                Toast.makeText(context, "${if (index>=0) "更新" else "新增"} 了-《${chapterVo.chapterName}》", Toast.LENGTH_SHORT).show()
                postValue(it)
            }
        }
    }

    override fun doCreateView(savedInstanceState: Bundle?) {
        btnFold
        btnExpand
        initFrameLayout()
        initTitleRecycleView()
        leftDrawerPopupView
        chapterDragView?.parent=leftDrawerPopupView
        mContentRecycleView.invalidate()
        initObservable()
        launch {
            delay(1000)
            loadData()
        }
    }

    private fun loadData() {
        var needLoad=true
        vm.listChapter.value?.takeIf { it.size>3 }?.let {
            needLoad=false
        }
        if(needLoad)randomData()
    }
    private fun randomData(){
        val list= mutableListOf<ChapterVo>()
        Toast.makeText(context, "初始化生成了一些7 条假数据", Toast.LENGTH_SHORT).show()
        for (i in 0..7) {
            list.add(ChapterVo("章节$i", locked = random(), index = i).apply {
                val l = mutableListOf<NewsVo>()
                for (j in 0..20.rad) {
                    l.add(NewsVo("随机$j").apply {
                        val ll = mutableListOf<ContentVo>()
                        for (m in 0..20.rad) {
                            ll.add(ContentVo("随机$m", "sss"))
                        }
                        listContent = ll.toList()
                    })
                }
                listNews = l.toList()
            })
        }
        vm.listChapter.postValue(list)
        vm.currChapter.postValue(list.first())
    }

    private fun random()=
        Math.random()>0.5
    //绑定监控数据变化的逻辑
    private fun initObservable() {
        vm.apply {
            listChapter.observe(this@HomeFragment){
                chapterDragView?.adapter
            }
            currChapter.observe(this@HomeFragment){
                binding.apply {
                    suoTextViewId.text=it.getLockStr()
                    ddTextViewId.text=it.chapterName
                    txTextViewId.text=it.description
                }
                titleAdapter.setmDatas(it.listNews?.map { it.title }?.apply {
                    if(size<=ColumnNum){ btnFold.visibility=View.GONE}
                })
                mContentAdapter.setData(it.listNews)
            }
        }
    }

    /**
     * 设置页面点击响应事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initFrameLayout() {
        binding.apply {
            cyImagebuttonId.setOnClickListener {//todo 左侧弹窗按钮
//                showLeftPopup()
                }
            szImagebuttonId.setOnClickListener {navigationEdit() }
        }


    }


    private fun navigationEdit() {
        try {
            leftDrawerPopupView.dismiss()
        }catch (e: Exception){
            Log.e("TAG", "navigationEdit:$e ")
        }
        //todo
//        NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_navigation_test_to_navigation_add_section, Bundle().apply {
//            putBoolean(Constants.IsEdit, true)
//            Log.e("TAG", "initFrameLayout: true")
//            putSerializable(Constants.ChapterVo, vm.currChapter.value)
//        })
    }

    private var leftPopup: BasePopupView?=null
    private val SINGLE_LINE = 0
    private val HALF_EXPANDED = 1
    private val COMPLETE_EXPANDED = 2

    //标题栏最大列数
    val ColumnNum = 4


    //顶部 抽屉展开按钮
    val btnExpand by lazy { binding.btnExpand.apply {
        Log.e(TAG, ": 抽屉展开按钮", )
        setOnClickListener {
            if(vm.currChapter?.value?.listNews?.size?:0<=ColumnNum){
                log("没有更多了")
                return@setOnClickListener
            }
            if (currTitleState == COMPLETE_EXPANDED) {
                if (supportHalfExpand) updateTitleButtons(HALF_EXPANDED)
                else updateTitleButtons()
            } else if (currTitleState == HALF_EXPANDED) {
                updateTitleButtons(COMPLETE_EXPANDED)
            } else {
                if (supportHalfExpand) updateTitleButtons(HALF_EXPANDED)
                else updateTitleButtons(COMPLETE_EXPANDED)
            }
        }
    } }

    private val mTitleRecycleView by lazy {
        binding.rvTitle.apply {
            layoutManager = titleLayoutManager
            adapter = titleAdapter
        }

    }
    //收起左侧弹窗按钮
    val btnFold by lazy {
        Log.e(TAG, ": 合上展开按钮", )
        binding.btnFold.also {
            it.setOnClickListener { updateTitleButtons() }
        }
    }

    //顶部标题栏布局管理
    private val titleLayoutManager by lazy {
        GridLayoutManager(requireContext(), ColumnNum * 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val end: Int = listData.size % ColumnNum
                    return if (listData.size - position <= end) ColumnNum * 3 / end else 3
                }
            }
        }
    }
    //顶部标题栏适配器
    val titleAdapter by lazy {
        ListDragAdapter(activity, listData).apply {
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
    //内容列表试图
    private val mContentRecycleView by lazy {
        binding.rvContentList.apply { adapter = mContentAdapter
           setOnScrollChangeListener { _, _, _, _, _ ->
               val linearLayoutManager = layoutManager as LinearLayoutManager
               val topPosition = linearLayoutManager.findFirstVisibleItemPosition()
               //设置滚动监听 使当前显示的第一个item 是标题栏的第一个
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
        setContentView(R.layout.bottom_dialog_style)
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
                    listData[currSelectedPosition] = text?:"-"
                    titleAdapter.notifyItemChanged(currSelectedPosition)
                    mContentAdapter.notifyItemChanged(currSelectedPosition)
            }.show()
        }
        findViewById<View>(R.id.tv_delete)?.setOnClickListener { v: View? ->
            if(editChapter){
                editChapter=false
                vm.listChapter.apply {
                    val index =value?.indexOf(vm.currChapter.value)
                    index?.takeIf { it>=0 }?.let {
                        val list =value?.also { it.removeAt(index ?: 0) }
                        postValue(list)
                    }
                }
            }else{
                Log.e("TAG", "initBottomSheetDiaLog: $currSelectedPosition")
                listData.removeAt(currSelectedPosition)
                titleAdapter.notifyItemRemoved(currSelectedPosition)
                mContentAdapter.notifyItemRemoved(currSelectedPosition)
            }
            dismiss()

        }
    } }
    //首页的左侧弹窗列表
    private val leftDrawerPopupView by lazy {
        ChapterPopupView(requireContext()).apply {
            llContainer.apply {
                chapterDragView=ChapterDragView(context, vm.listChapter).apply {
                    layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
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
                                Log.e(TAG, "onAfterPressMove: ", )
                                longPress = false
                                bottomSheetDialog.dismiss()
                            }
                        }
                    liveDate.observe(this@HomeFragment){
                        mAdapter.setData(it)
                    }
                }.also { addView(it) }
            }
            mOnClickListener= View.OnClickListener {
                //Todo 跳转页面
//                NavHostFragment.findNavController(this@HomeFragment)
//                        .navigate(R.id.action_navigation_test_to_navigation_add_section, Bundle().apply {
//                            putBoolean(Constants.IsEdit, false)
//                        }
//                        )
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
    val listData= getRandomData(17)
    private val mContentAdapter by lazy {
        NewsContentAdapter(context, listOf())
    }

    private var longPress = false


    //初始化上部小标题列表栏
    private fun initTitleRecycleView() {
        val itemTouchHelper = ItemTouchHelper(ItemDragHelperCallBack(object : ItemHelper {
            override fun itemMoved(oldPosition: Int, newPosition: Int) {
                log("move")
                //交换变换位置的集合数据
                Collections.swap(titleAdapter.data, oldPosition, newPosition)
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
                    longPress = false
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
            log(String.format("height %d", mTitleRecycleView.measuredHeight))
            if (mTitleRecycleView.measuredHeight > titleMaxHeight) {
                supportHalfExpand = true
                updateTitleButtons(HALF_EXPANDED)
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


    /**
     * 根据顶部标题栏折叠状态，显示标题栏按钮
     * 刷新标题栏按钮显示
     */
    private fun updateTitleButtons(newState:Int=SINGLE_LINE) {
        currTitleState=newState
        val layoutParams = mTitleRecycleView.layoutParams as LinearLayout.LayoutParams
        when (newState) {
            SINGLE_LINE -> {
                layoutParams.height = singleLineHeight
                mTitleRecycleView.layoutParams = layoutParams
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                btnFold.visibility = View.GONE
            }
            HALF_EXPANDED -> {
                layoutParams.height = titleMaxHeight
                mTitleRecycleView.layoutParams = layoutParams
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                btnFold.visibility = View.VISIBLE
            }
            COMPLETE_EXPANDED -> {
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                mTitleRecycleView.layoutParams = layoutParams
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
                btnFold.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }

}