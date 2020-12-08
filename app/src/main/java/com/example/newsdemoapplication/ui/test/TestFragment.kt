package com.example.newsdemoapplication.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.TestFragmentBinding
import com.google.android.material.navigation.NavigationView
import com.gzp.baselib.base.MvvmBaseFragment

class TestFragment : MvvmBaseFragment<TestViewModel,TestFragmentBinding>() {

    var flag = false
    var suo_textView: TextView? = null
    var ts_textView: TextView? = null
    var ts1_textView: TextView? = null
    var sz_imagebutton: ImageButton? = null
    var cy_imagebutton: ImageButton? = null
    var dl: DrawerLayout? = null
    var navigationView: NavigationView? = null
    var linearLayout1: LinearLayout? = null
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
    }

}