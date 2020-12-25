package com.example.newsdemoapplication.ui.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.AddSectionFragmentBinding
import com.example.newsdemoapplication.vo.ChapterVo
import com.example.newsdemoapplication.vo.ContentVo
import com.example.newsdemoapplication.vo.NewsVo
import com.gzp.baselib.base.MvvmBaseFragment
import com.gzp.baselib.constant.Constants
import com.gzp.baselib.widget.NavigationBar
import com.lishuaihua.toast.ToastUtils.show
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
                    chapterVo?.let { loadData(it)}
                } else "添加"}章节".let {
                    setTitle(it)
                }
                setBackOnClickListener{
                    NavHostFragment.findNavController(this@AddSectionFragment)
                            .navigate(R.id.navigation_test)
                }
            }
            if(chapterVo==null)(binding.titleBar as NavigationBar).apply {
                "添加章节".let {
                    setTitle(it)
                }}
            binding.btnAdd.setOnClickListener {
                binding.etChapterTitle.text?.let {
                    if(it.isNullOrEmpty()) Toast.makeText(context, "先输入标题", Toast.LENGTH_SHORT).show()
                    else{
                        Toast.makeText(context, "${it} 添加成功", Toast.LENGTH_SHORT).show()
                        if(chapterVo==null) chapterVo= ChapterVo("")
                        val list = mutableListOf<NewsVo>().apply { chapterVo?.listNews?.forEach{add(it) } }
                        list.add(NewsVo(it.toString()))
                        chapterVo?.let {
                            it.listNews =list.toList()
                        }
                    }
                }
//                XPopup.Builder(context).asInputConfirm("输入内容",""){}.show()
            }
            binding.btnDeleteTitle.setOnClickListener {
                binding.etChapterTitle.text?.let {ed->
                    if(ed.isEmpty()) Toast.makeText(context, "先输入标题", Toast.LENGTH_SHORT).show()
                    else{
                        if(chapterVo==null) chapterVo= ChapterVo("")
                        val list = mutableListOf<NewsVo>().apply { chapterVo?.listNews?.run {
                            firstOrNull{it.title==ed.toString()}?:show("没有 $ed").also { return@setOnClickListener }
                            filter { it.title!=ed.toString() }.forEach{add(it) }
                        }
                        }
                        list.add(NewsVo(it.toString()))
                        chapterVo?.let {
                            it.listNews =list.toList()
                        }
                        Toast.makeText(context, "$ed 删除成功", Toast.LENGTH_SHORT).show()
                    }
                }
//                XPopup.Builder(context).asInputConfirm("输入内容",""){}.show()
            }
            binding.btnAddContent.setOnClickListener {
                binding.etContentTitle.text?.let {content ->
                    if(binding.etChapterTitle.text.isNullOrEmpty())Toast.makeText(context, "先输入标题", Toast.LENGTH_SHORT).show().also { return@setOnClickListener }
                    binding.etChapterTitle.text?.let {ed ->
                        if(chapterVo==null) chapterVo= ChapterVo("")
                        val list = mutableListOf<NewsVo>().apply { chapterVo?.listNews?.forEach{add(it) } }
                        list.firstOrNull { it.title==ed.toString() }?.let {
                            if(content.isNullOrEmpty()) Toast.makeText(context, "先输入内容", Toast.LENGTH_SHORT).show()
                            else{
                                Toast.makeText(context, "${content} 添加成功", Toast.LENGTH_SHORT).show()
                                val listContent = mutableListOf<ContentVo>().apply { it.listContent?.forEach{add(it) } }
                                listContent.add(ContentVo(content.toString(),content = content.toString()))
                                it.listContent=listContent.toList()
                                chapterVo?.let {
                                    it.listNews =list.toList()
                                }
                            }
                        }?:Toast.makeText(context, "先选择标题", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            binding.btnDeleteContent.setOnClickListener {
                binding.etContentTitle.text?.let {ed->
                    if(ed.isEmpty()) Toast.makeText(context, "先输入内容", Toast.LENGTH_SHORT).show()
                    else{
                        binding.etContentTitle.text?.let {content ->
                            if(chapterVo==null) chapterVo= ChapterVo("")
                            val list = mutableListOf<NewsVo>().apply { chapterVo?.listNews?.forEach{add(it) } }
                            list.firstOrNull { it.title==ed.toString() }?.let {
                                if(content.isNullOrEmpty()) Toast.makeText(context, "先输入内容", Toast.LENGTH_SHORT).show()
                                else{
                                    val listContent = mutableListOf<ContentVo>().apply {
                                        it.listContent?.firstOrNull { it.equals(content) }?:show("没有 $ed").also { return@setOnClickListener }
                                        it.listContent?.forEach{if(it.title != content.toString())add(it) } }
                                    listContent.add(ContentVo(content.toString(),content = content.toString()))
                                    it.listContent=listContent.toList()
                                    chapterVo?.let {
                                        it.listNews =list.toList()
                                    }
                                    Toast.makeText(context, "${content} 添加成功", Toast.LENGTH_SHORT).show()

                                }
                            }?:Toast.makeText(context, "先选择标题", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
//                XPopup.Builder(context).asInputConfirm("输入内容",""){}.show()
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