package com.example.newsdemoapplication.ui.add

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.AddSectionFragmentBinding
import com.gzp.baselib.base.MvvmBaseFragment
import com.gzp.baselib.constant.Constants
import com.gzp.baselib.widget.NavigationBar

class AddSectionFragment :MvvmBaseFragment<AddViewModel,AddSectionFragmentBinding>() {
    override fun getLayoutResId()= R.layout.add_section_fragment
    private val isEdit =arguments?.getBoolean(Constants.IsEdit,false)?:false

    override fun doCreateView(savedInstanceState: Bundle?) {
        (binding.titleBar as NavigationBar).apply {
            setTitle("${if (isEdit) "管理" else "添加"}章节")
            setBackOnClickListener{
                NavHostFragment.findNavController(this@AddSectionFragment)
                        .popBackStack()
            }
        }
        binding.btnSave.setOnClickListener {
            NavHostFragment.findNavController(this@AddSectionFragment)
                    .popBackStack()
        }

    }
}