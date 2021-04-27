package com.example.newsdemoapplication.ui

import android.os.Bundle
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.FragmentBlackBinding
import com.example.newsdemoapplication.ui.manager.AddViewModel
import com.example.newsdemoapplication.util.common.MvvmBaseFragment

class BlackFragment: MvvmBaseFragment<AddViewModel, FragmentBlackBinding>() {
    private val TAG =this::class.java.simpleName

    override fun getLayoutResId()= R.layout.fragment_black

    override fun doCreateView(savedInstanceState: Bundle?) {
//        TODO("Not yet implemented")
    }
}