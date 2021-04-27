package com.example.newsdemoapplication.ui.manager

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.newsdemoapplication.Constants
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.AddSectionFragmentBinding
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.model.ContentVo
import com.example.newsdemoapplication.model.NewsVo
import com.example.newsdemoapplication.util.common.MvvmBaseFragment
import com.lishuaihua.toast.ToastUtils.show

/**
 * 添加 编辑一级标题的页面
 * @author gzp
 */
class AddSectionFragment : MvvmBaseFragment<AddViewModel, AddSectionFragmentBinding>() {
    override fun getLayoutResId()= R.layout.add_section_fragment
    private val isEdit by lazy{ //编辑还是新增的状态
        arguments?.getBoolean(Constants.IsEdit,false)?:false
    }
    private var chapterVo : ChapterVo?=null

    override fun doCreateView(savedInstanceState: Bundle?) {
        val tvTitle =binding.titleBar.findViewById<TextView>(R.id.tv_title)
        val btnBack:ImageView =binding.titleBar.findViewById(R.id.btn_back)
            btnBack.setOnClickListener {pop()}
        tvTitle.apply {
            ("${if (isEdit) "管理".apply {
                chapterVo=arguments?.getSerializable(Constants.ChapterVo)?.let { it as ChapterVo }
                chapterVo?.let { 
                    binding.etChapterName.setText(it.chapterName)
                    binding.etChapterPosition.setText(it.index.toString())
                }} 
            else "添加"}一级标题").let { text=it }
        }
        if(chapterVo==null)
            tvTitle.apply {
                text="添加一级标题"
            }
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
                            listContent.add(ContentVo(content.toString(), content = content.toString()))
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
                                listContent.add(ContentVo(content.toString(), content = content.toString()))
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
        }
        binding.btnSave.setOnClickListener {
            binding.etChapterName.text.let {ed->
                if(ed.isNullOrEmpty()) Toast.makeText(context, "请先输入一级标题名称", Toast.LENGTH_SHORT).show()
                else{
                    if(chapterVo==null) chapterVo= ChapterVo(ed.toString())
                    chapterVo?.apply {
                        chapterName =ed.toString()
                        index=binding.etChapterPosition.text?.toString()?.toIntOrNull()?: Int.MAX_VALUE
                    }
//                        NavHostFragment.findNavController(this@AddSectionFragment)
//                                .navigate(R.id.navigation_test,Bundle().apply {
//                                    putSerializable(Constants.ChapterVo,chapterVo)
//                                })
                }
            }
        }
    }
}