package com.example.newsdemoapplication.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.HomeV2FragmentBinding
import com.example.newsdemoapplication.databinding.NextBottonDialogBinding
import com.example.newsdemoapplication.dsl.*
import com.example.newsdemoapplication.model.room.Chapter
import com.example.newsdemoapplication.util.toast
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.view.DragRecycleView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gzp.baselib.base.BaseViewModel
import com.gzp.baselib.base.MvvmBaseFragment
import com.lxj.xpopup.XPopup
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeV2Fragment: MvvmBaseFragment<BaseViewModel, HomeV2FragmentBinding>(){
    private val homeV2ViewModel:HomeV2ViewModel by viewModel()
    val detailViewModel:ChapterDetailViewModel by viewModel()
    var selectedChapterId=0L
    lateinit var chapterDetailView: ChapterDetailView
    lateinit var chapterListView: ChapterDragView<Chapter>
    override fun getLayoutResId()= R.layout.home_v2_fragment
    private val nextSheetDialog by lazy { BottomSheetDialog(requireContext()).apply {
        val binding :NextBottonDialogBinding= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.next_botton_dialog,null,false)
        setContentView(binding.root)
        setCancelable(true)
        with(binding.tvContent){
            addOnClick { dismiss() }
            text="高度依据内容自适应\n高度依据内容自适应\n高度依据内容自适应\n高度依据内容自适应\n高度依据内容自适应\n高度依据内容自适应\n"
        }
    } }
    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.apply {
            with(titleBar){
                btnBack.addOnClick { drawerLayout.openDrawer(leftId)
                btnRight.addOnClick {
                    homeV2ViewModel.insert(homeV2ViewModel.factoryChapter().toast("addChapter-->"))
                }
                tvTitle.addOnClick { homeV2ViewModel.deleteAll().toast("delete--》") }
                observe(homeV2ViewModel.currSelectChapter){
                    tvTitle.text=it.name
                    selectedChapterId=it.id
                    chapterDetailView.updateChapterId(selectedChapterId)
                }
            }
            leftId.
                addOnClick { toast("nothing") }
            with(llContainer){
                chapterDetailView=ChapterDetailView(this@HomeV2Fragment).apply {
                    layout_width= match_parent
                    layout_height= match_parent
                }.also { addView(it) }
            }

            with(leftId.llContainer){
                chapterListView=ChapterDragView(context, homeV2ViewModel.getAll()).apply {
                    layoutParams= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                    setBackgroundColor(Color.parseColor("#234567"))
                    clickCallBack=object : ChapterDragView.ClickCallBack<Chapter>{
                        override fun onItemClicked(chapterVo: Chapter) {
                            homeV2ViewModel.currSelectChapter.value=chapterVo
                        }
                    }
                    mCallBack=object: DragRecycleView.DragAndPressCallBack {
                        //设置移动监听回调
                        override fun onLongPress() {
                            "长按底部弹窗".toast
//                            editChapter =true
//                            bottomSheetDialog.show()
                        }
                        override fun onAfterPressMove() {
                            "移动松手操作".toast
                        }
                    }
                    observe(liveDate){
                        homeV2ViewModel.currSelectChapter.postValue( it[mAdapter.currSelectPosition])
                        tvTitle.text= it[mAdapter.currSelectPosition].name
                        selectedChapterId= it[mAdapter.currSelectPosition].id
                    }
                }.also { addView(it) }
            }
            with(btnNext){
                bgShape(6,R.color.blue)
                addOnClick { nextSheetDialog.show() }
            }
        }
    }
    }

    override fun doObservable() {

    }



}