package com.example.newsdemoapplication.ui.home

import android.os.Bundle
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.HomeV2FragmentBinding
import com.example.newsdemoapplication.dsl.invisible
import com.gzp.baselib.base.BaseViewModel
import com.gzp.baselib.base.MvvmBaseFragment

class HomeV2Fragment: MvvmBaseFragment<BaseViewModel, HomeV2FragmentBinding>(){
    override fun getLayoutResId()= R.layout.home_v2_fragment
    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.apply {
            titleBar.btnBack.visibility= invisible
        }
    }
}