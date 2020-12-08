package com.example.newsdemoapplication.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.TestFragmentBinding
import com.google.android.material.navigation.NavigationView
import com.gzp.baselib.base.MvvmBaseFragment

class TestFragment : MvvmBaseFragment<TestViewModel,TestFragmentBinding>() {

    companion object {
        fun newInstance() = TestFragment()
    }

    private lateinit var viewModel: TestViewModel

    override fun getLayoutResId()=R.layout.test_fragment

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.btnCloseRight.apply {
            setOnClickListener { showLoading() }
        }
        binding.frameLayout.apply {
            cyImagebuttonId.setOnClickListener {
                binding.drawerLayout.openDrawer(binding.linearLayout1Id)
            }
            szImagebuttonId.setOnClickListener{
                binding.drawerLayout.openDrawer(binding.leftNavView)
            }
        }
        binding.drawerLayout.findViewById<TextView>(R.id.textView)?.let{
            it.setOnClickListener { binding.drawerLayout.closeDrawer(binding.leftNavView) }
        }
        binding.btnClose.setOnClickListener {
            it.setOnClickListener { binding.drawerLayout.closeDrawer(binding.leftNavView) }
        }
        binding.btnAdd.setOnClickListener {

        }
    }

}