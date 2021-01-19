package com.example.newsdemoapplication.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.dsl.*

class PhotoViewPagerFragment:Fragment() {
    private val contentView by lazy {
        LinearLayout{
            layout_height=match_parent
            layout_width= match_parent
            bgShape(10, R.color.blue)

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return contentView
    }
}