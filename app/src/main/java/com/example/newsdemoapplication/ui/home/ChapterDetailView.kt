package com.example.newsdemoapplication.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.adapter.ListSubsectionDragAdapter
import com.example.newsdemoapplication.callback.OnItemClickListener
import com.example.newsdemoapplication.databinding.ChapterDetailViewBinding
import com.example.newsdemoapplication.dsl.addOnClick
import com.example.newsdemoapplication.dsl.observe
import com.example.newsdemoapplication.util.log
import com.example.newsdemoapplication.util.toast

@SuppressLint("ViewConstructor")
class ChapterDetailView(val parent:HomeV2Fragment):FrameLayout(parent.requireContext()) {
    val columnNum = 4
    private val viewModel by lazy { parent.detailViewModel }
    val binding:ChapterDetailViewBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(parent.requireContext()), R.layout.chapter_detail_view,null,false)
    }
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
        ListSubsectionDragAdapter(context, list).apply {
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
    init {
        addView(binding.root)
        with(binding.rvTitle){
            layoutManager = titleLayoutManager
            adapter = titleAdapter
        }
        binding.btnAddSubsection.addOnClick {
            viewModel.insertSubsection(viewModel.factorySubsection(parent.selectedChapterId).toast)
            viewModel.getSubsectionListByChapterId(parent.selectedChapterId.log)
//            observe(
//                    viewModel.getSubsectionList(parent.selectedChapterId.log)){
//                viewModel.currSubsectionList.postValue(it.log)
//            }
        }
        binding.btnFold.addOnClick{viewModel.deleteAllSubsection().toast("清理完毕")}

        binding.rvTitle.observe(viewModel.currSubsectionList){
            list=it.toast
            titleAdapter.setmDatas(list)
        }
        updateChapterId(parent.selectedChapterId)
    }
    fun updateChapterId(id:Long){
        viewModel.getSubsectionListByChapterId(id)
    }
    private fun contentScrollToPosition(position: Int) {
        val mLayoutManager = binding.rvContentList.layoutManager as LinearLayoutManager
        mLayoutManager.scrollToPositionWithOffset(position, 0)
    }

}