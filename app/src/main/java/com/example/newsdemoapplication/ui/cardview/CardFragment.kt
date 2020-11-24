package com.example.newsdemoapplication.ui.cardview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.ui.home.HomeViewModel
import com.example.newsdemoapplication.ui.login.LoginActivity

class CardFragment :Fragment(){
    private  var homeViewModel:HomeViewModel?=null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root: View = inflater.inflate(R.layout.fragment_cart, container, false)
        val textView = root.findViewById<TextView>(R.id.btn_login)
        return root
    }
}