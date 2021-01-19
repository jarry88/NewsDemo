package com.example.newsdemoapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.dsl.*
import com.example.newsdemoapplication.room.Word
import com.lishuaihua.toast.ToastUtils

class RoomFragment :Fragment(){
    private val wordDb by lazy {
        (requireActivity().application as NewsApp).wordDb.getWordDao()
    }
    var count =0
    var name ="$"
    private val contentView by lazy {
        LinearLayout{
            layout_height= match_parent
            layout_width= match_parent
            bgShape(10, R.color.blue)
            TextView { weight=1f
            text= "get"
            padding=10
            bgShape(6,R.color.light_green)}.addOnClick {
                ToastUtils.show("getwordDao")
                Log.e("TAG", ".:${wordDb.getAllWords().toString()} ", )
            }
            TextView { weight=1f
            text= "insert"
            padding=10
            bgShape(6,R.color.light_green)}.addOnClick {
                ToastUtils.show("addwordDao")
                wordDb.insertWord(Word(count++,"$$count",name.also { name+=name }))
            }
            TextView { weight=1f
            text= "insert"
            padding=10
            bgShape(6,R.color.light_green)}.addOnClick {
                ToastUtils.show("getwordDao")
                (requireActivity().application as NewsApp).wordDb.getWordDao()
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return contentView
    }
}