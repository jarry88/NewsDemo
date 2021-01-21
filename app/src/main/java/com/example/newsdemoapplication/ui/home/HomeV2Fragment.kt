package com.example.newsdemoapplication.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.HomeV2FragmentBinding
import com.example.newsdemoapplication.dsl.addOnClick
import com.example.newsdemoapplication.dsl.bg_id
import com.example.newsdemoapplication.dsl.invisible
import com.example.newsdemoapplication.dsl.observe
import com.example.newsdemoapplication.model.room.Chapter
import com.example.newsdemoapplication.util.randomUrl
import com.example.newsdemoapplication.util.toast
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.view.DragRecycleView
import com.example.newsdemoapplication.vo.ChapterVo
import com.gzp.baselib.base.BaseViewModel
import com.gzp.baselib.base.MvvmBaseFragment
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeV2Fragment: MvvmBaseFragment<BaseViewModel, HomeV2FragmentBinding>(){
    private val homeV2ViewModel:HomeV2ViewModel by viewModel()
    override fun getLayoutResId()= R.layout.home_v2_fragment
//    fun getChapter()=var a inject<Chapter> ()
    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.apply {
            with(titleBar){
                btnBack.addOnClick { drawerLayout.openDrawer(leftId)
                btnRight.bg_id=R.drawable.y
                btnRight.addOnClick {
                    homeV2ViewModel.insert(Chapter(1,1,"SSS",false, randomUrl()))
                }
                    invalidate()
            }

            with(leftId.llContainer){
                ChapterDragView(context, homeV2ViewModel.getAll()).apply {
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
//                            Log.e(TAG, "onAfterPressMove: ", )
//                            longPress = false
//                            bottomSheetDialog.dismiss()
                        }
                    }
                    observe(liveDate){
                        mAdapter.setData(it)
                    }
                }.also { addView(it) }
            }
        }
    }
}

}