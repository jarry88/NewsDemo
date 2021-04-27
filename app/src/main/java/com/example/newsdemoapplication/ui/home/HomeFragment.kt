package com.example.newsdemoapplication.ui.home

import android.annotation.SuppressLint
import android.app.Service
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.newsdemoapplication.Constants
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.HomeFragmentBinding
import com.example.newsdemoapplication.view.ListDragAdapter
import com.example.newsdemoapplication.view.ListDragAdapter.MyDragViewHolder
import com.example.newsdemoapplication.util.callback.ItemHelper
import com.example.newsdemoapplication.util.CommonUtils
import com.example.newsdemoapplication.util.callback.ItemDragHelperCallBack
import com.example.newsdemoapplication.util.callback.OnItemClickListener
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.view.TitleRecycleView.DragAndPressCallBack
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.ui.BlackFragment
import com.example.newsdemoapplication.ui.main_activity.MainActivity
import com.example.newsdemoapplication.ui.manager.AddSectionFragment
import com.example.newsdemoapplication.util.callback.SimpleCallback
import com.example.newsdemoapplication.util.common.MvvmBaseFragment
import com.example.newsdemoapplication.util.getRandomData
import com.example.newsdemoapplication.util.log
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lxj.xpopup.XPopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.util.*

/**
 * app首页
 * @author gzp
 */
class HomeFragment : MvvmBaseFragment<HomeViewModel, HomeFragmentBinding>(),CoroutineScope by MainScope(),SimpleCallback {
    private val TAG =this::class.java.simpleName
    override fun getLayoutResId()=R.layout.home_fragment
    val mainActivity by lazy { activity as MainActivity }
    //标题栏最大列数
    val ColumnNum = 4
    //顶部标题栏折叠状态
    private val SINGLE_LINE = 0
    private val HALF_EXPANDED = 1
    private val COMPLETE_EXPANDED = 2

    private var currTitleState = SINGLE_LINE


    private var editChapter: Boolean=false
    val chapterDragView:ChapterDragView by lazy { mainActivity.binding.zuoyeId.listView.also {
        it.mAdapter.selectCallback=this
        it.mCallBack = object : DragAndPressCallBack {
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
    }}

    var currContentNewsItem=0
    lateinit var mFragmentAdapter:FragmentStateAdapter
    //回到页面时更新数据
    override fun onResume() {
        super.onResume()
        log("resume")
        arguments?.getSerializable(Constants.ChapterVo)?.let {
            vm.saveChapter(it as ChapterVo)
        }
    }

    override fun doCreateView(savedInstanceState: Bundle?) {
        btnFold //初始化 首页顶部标题栏 收起按钮
        btnExpand //初始化 首页顶部标题栏 向下展开按钮
        initView()
        initTitleRecycleView()
        initObservable()
        launch {
            delay(500)
            vm.randomData()
        }
    }


    /**
     * 设置页面点击响应事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        mainActivity.binding.zuoyeId.tjanjiadidianAn.setOnClickListener {
            mainActivity.closeDrawerLayout()
            start(AddSectionFragment())
        }
        binding.apply {
            mFragmentAdapter=object :FragmentStateAdapter(this@HomeFragment){
                override fun getItemCount()=currContentNewsItem
                override fun createFragment(position: Int): ScrollSliderFragment {
                    val fragment = ScrollSliderFragment()
                    fragment.arguments = Bundle().apply {
                        putInt(Constants.SLIDE_OBJECT, position )
                    }
                    fragment.newsVo=vm.findNewsVo(position)
                    return fragment//.also { it.update(vm.currChapter.value?.listNews,position) }
                }
            }
            pager.adapter=mFragmentAdapter
            pager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    log("currSelectPosition ${titleAdapter.currSelectPosition}滚到 $position")
                    if (titleAdapter.currSelectPosition != position) {
                        titleAdapter.currSelectPosition=position
                        titleLayoutManager.scrollToPositionWithOffset(position,0)
                    }
                }
            })
            (activity as? MainActivity)?.let {a->
                llTopLeft.setOnClickListener {// 左侧弹窗按钮
                    a.mDrawerLayout.openDrawer(GravityCompat.START) }
                    chapterDragView.mAdapter.notifyDataSetChanged()
                llTopRight.setOnClickListener {
                    a.mDrawerLayout.openDrawer(GravityCompat.END)
                }
            }
            xntyTextViewId.setOnClickListener {  log("生成假数据")
                vm.randomData() }
        }

    }

    //监控到关键数据变化时的UI响应逻辑
    private fun initObservable() {
        vm.apply {
            listChapter.observe(this@HomeFragment){
                chapterDragView.mAdapter.list=it
                chapterDragView.mAdapter.notifyDataSetChanged()

                toastShort(it.size.toString())
            }
            currChapter.observe(this@HomeFragment){
                log("数据初始化")
                contentScrollToPosition(0)

                binding.apply {
                    suoTextViewId.text=it.getLockStr()
                    ddTextViewId.text=it.chapterName
                    txTextViewId.text=it.description
                }
                titleAdapter.setmDatas(it.listNews?.map { it.title }?.apply {
                    if(size<=ColumnNum){
                        btnFold.visibility=View.GONE
                        btnExpand.visibility=View.GONE
                    }else{
                        btnExpand.visibility=View.VISIBLE
                    }
                })
//                mContentAdapter.setData(it.listNews)
                mFragmentAdapter.notifyDataSetChanged()
            }
            toast.observe(this@HomeFragment){
                toastShort(it)
            }
        }
    }


    //顶部 抽屉展开按钮
    val btnExpand by lazy { binding.btnExpand.apply {
        Log.e(TAG, ": 抽屉展开按钮", )
        setOnClickListener {
//            if(vm.currChapter.value?.listNews?.size?:0<=ColumnNum){
//                log("没有更多了")
//                return@setOnClickListener
//            }
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
    //顶部标题列表栏视图
    private val mTitleRecycleView by lazy {
        binding.rvTitle.apply {
            layoutManager = titleLayoutManager
            adapter = titleAdapter
        }
    }
    //标题栏底部折叠按钮
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
    //底部弹窗设置
    val bottomSheetDialog by lazy { BottomSheetDialog(requireContext()).apply {
        setContentView(R.layout.bottom_dialog_style)
        setCancelable(true)
        findViewById<View>(R.id.tv_cancel)?.setOnClickListener {dismiss() }
        findViewById<View>(R.id.tv_rename)?.setOnClickListener {
            Log.e("TAG", "initBottomSheetDiaLog: $currSelectedPosition")
            dismiss()
//            val leftListOpen =mainActivity.mDrawerLayout.isDrawerOpen(mainActivity.binding.zuochoutiId)
            val leftListOpen =true
            if(editChapter&&! leftListOpen){
                editChapter=false //todo 跳转到编辑页
            }else
            XPopup.Builder(activity).asInputConfirm("重命名 $leftListOpen", "请输入内容。") { text: String? ->
                if(!leftListOpen){
                    listData[currSelectedPosition] = text?:"-"
                    titleAdapter.notifyItemChanged(currSelectedPosition)
                    mFragmentAdapter.notifyItemChanged(currSelectedPosition)
//                                        mContentAdapter.notifyItemChanged(currSelectedPosition)
                }else{
                    vm.currChapter.value?.let {
                        val a =it
                        a.chapterName=text?:"-"
                        vm.listChapter.value?.firstOrNull { it.id==a.id }?.let { it.chapterName=a.chapterName }
                        vm.currChapter.postValue(a)
                        vm.listChapter.value=vm.listChapter.value
                    }

                }
            }.show()
        }
        findViewById<View>(R.id.tv_delete)?.setOnClickListener { v: View? ->
//            val leftListOpen =mainActivity.mDrawerLayout.isDrawerOpen(mainActivity.binding.zuochoutiId)
            val leftListOpen =true
            if(editChapter&&! leftListOpen){
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

                if(leftListOpen){
                    vm.deleteChapter()
//                    mainActivity.mDrawerLayout.closeDrawer(mainActivity.binding.zuochoutiId)
                }else{
                    listData.removeAt(currSelectedPosition)
                    titleAdapter.notifyItemRemoved(currSelectedPosition)
                    mFragmentAdapter.notifyItemRemoved(currSelectedPosition)
                }

            }
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


    val listData= getRandomData(17)

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
       vm.currChapter.value?.listNews?.let {
           currContentNewsItem=it.size
       }

        log("${vm.currChapter.value}itemCount ${mFragmentAdapter.itemCount} 页")
        binding.pager.setCurrentItem(position,false)
        mFragmentAdapter.createFragment(position)
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
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                btnFold.visibility = View.GONE
            }
            HALF_EXPANDED -> {
                layoutParams.height = titleMaxHeight
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                btnFold.visibility = View.VISIBLE
            }
            COMPLETE_EXPANDED -> {
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                btnExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
                btnFold.visibility = View.VISIBLE
            }
            else -> {
            }
        }
        mTitleRecycleView.layoutParams = layoutParams
    }

    fun refreshDate() {
        vm.randomData()
    }

    override fun changeChapter(id: Int) {
        log("changeChapter $id")
        vm.changeChapter(id)
    }
}