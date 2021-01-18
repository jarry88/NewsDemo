package com.example.newsdemoapplication.ui.cardview

import android.content.Context
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
import com.lxj.xpopup.XPopup
import kotlin.reflect.full.primaryConstructor

class CardFragment :Fragment(){
    private  var homeViewModel:HomeViewModel?=null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root: View = inflater.inflate(R.layout.fragment_cart, container, false)
        val textView = root.findViewById<TextView>(R.id.btn_login)
        return root
    }
    fun a(){
        val c=1.builder1(requireContext())
        c{XPopup.Builder(it)
        2}
        val m=builder<XPopup.Builder>(requireContext()).invoke{XPopup.Builder(it)}
        val build=build { XPopup.Builder(it) }
        XPopup.Builder(context).asCustom(XPopup.Builder(context).asLoading())
                .show()
        val t =xBuild(requireContext())
        val t1 =xBuild2(requireContext())
        t(context)
        t1()
    }
    fun <T:Any> T.builder1(context: Context)={build:(Context)->T -> build(context).also { println(context) }}
    fun <T:Any> builder(context: Context)={build:(Context)->T -> build(context).also { println(context) }}
    fun <T> build(b:(Context)->T)=b(requireContext())
    fun b(context: Context)= run { XPopup.Builder(context) }
    fun  xBuild(context: Context)=  {_:Context? ->XPopup.Builder(context)}
    fun  xBuild2(context: Context)=  { -> XPopup.Builder(context) }
}