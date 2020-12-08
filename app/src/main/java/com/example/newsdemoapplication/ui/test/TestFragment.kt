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
import com.google.android.material.navigation.NavigationView

class TestFragment : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TestViewModel::class.java)

    }

}