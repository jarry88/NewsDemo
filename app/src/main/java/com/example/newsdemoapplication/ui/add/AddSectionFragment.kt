package com.example.newsdemoapplication.ui.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.AddSectionFragmentBinding
import com.example.newsdemoapplication.vo.ChapterVo
import com.gzp.baselib.base.MvvmBaseFragment
import com.gzp.baselib.constant.Constants
import com.gzp.baselib.widget.NavigationBar
import com.lxj.xpopup.XPopup

class AddSectionFragment :MvvmBaseFragment<AddViewModel,AddSectionFragmentBinding>() {
    override fun getLayoutResId()= R.layout.add_section_fragment
    private val isEdit by lazy{
        arguments?.getBoolean(Constants.IsEdit,false)?:false
    }
        private var chapterVo :ChapterVo?=null

        override fun doCreateView(savedInstanceState: Bundle?) {
            (binding.titleBar as NavigationBar).apply {
                "${if (isEdit) "管理".apply {
                    chapterVo=arguments?.getSerializable(Constants.ChapterVo)?.let { it as ChapterVo }
                    chapterVo?.let {
                        loadData(it)}
                } else "添加"}章节".let {
                    setTitle(it)
                }
                setBackOnClickListener{
                    NavHostFragment.findNavController(this@AddSectionFragment)
                            .navigate(R.id.navigation_test)
                }
            }
            binding.btnAdd.setOnClickListener {
                XPopup.Builder(context).asInputConfirm("输入内容",""){}.show()

            }
            binding.btnSave.setOnClickListener {
                binding.etChapterName.text.let {ed->
                    if(ed.isNullOrEmpty()) Toast.makeText(context, "请先输入章节名称", Toast.LENGTH_SHORT).show()
                    else{
                        if(chapterVo==null) chapterVo= ChapterVo(ed.toString())
                        chapterVo?.apply {
                            chapterName =ed.toString()
                            index=binding.etChapterPosition.text?.toString()?.toIntOrNull()?: Int.MAX_VALUE
                        }
                        NavHostFragment.findNavController(this@AddSectionFragment)
                                .navigate(R.id.navigation_test,Bundle().apply {
                                    putSerializable(Constants.ChapterVo,chapterVo)
                                })
                    }
                }
            }

        }

        private fun loadData(chapterVo: ChapterVo) {
            binding.apply {
                etChapterName.setText(chapterVo.chapterName)
                etChapterPosition.setText(chapterVo.index.toString())
            }
        }
}