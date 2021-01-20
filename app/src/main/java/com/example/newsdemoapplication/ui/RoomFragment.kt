package com.example.newsdemoapplication.ui

import android.os.Bundle
import android.util.Log
import android.util.SparseLongArray
import android.util.StatsLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.dsl.*
import com.example.newsdemoapplication.main.MainActivity
import com.example.newsdemoapplication.main.MainActivity1
import com.example.newsdemoapplication.room.Word
import com.example.newsdemoapplication.widget.PageTitleBar
import com.lishuaihua.toast.ToastUtils
import com.lxj.xpopup.XPopup

class RoomFragment :Fragment(){
    private val mainViewModel by lazy {
        (requireActivity() as? MainActivity1)?.mainViewModel!!
    }
    private val allWord by lazy {
        mainViewModel.allWord
    }
    var count =0L
    var name ="$"
    private val queryId by lazy {
        MutableLiveData(0L)
    }
    private val contentView by lazy {
        LinearLayout {
            padding=10
            layout_height= match_parent
            layout_width= match_parent
            orientation= vertical
            PageTitleBar(context).also {
                it.tvTile.text="点击标题后进入的新页面，后续按需求补充"
                it.btnBack.addOnClick { NavHostFragment.findNavController(this@RoomFragment).popBackStack() }
                addView(it) }
            LinearLayout {
                LinearLayout{
                    layout_height= match_parent
                    layout_width= wrap_content
                    orientation= vertical
                    bgShape(10, R.color.blue)
                    TextView { weight=1f
                        text= "get"
                        padding=10
                        bgShape(6,R.color.light_green)}.addOnClick {
                        ToastUtils.show("getwordDao")
                        Log.e("TAG", "${allWord.value?.size}.:${allWord.value.toString()} ", )
                    }
                    TextView { weight=1f
                        text= "insert"
                        padding=10
                        bgShape(6,R.color.light_green)}.addOnClick {
                        ToastUtils.show("addwordDao")
                        mainViewModel.insert(Word(count++,"$$count",name.also { name+=name }))
                    }
                    TextView { weight=1f
                        text= "insert"
                        padding=10
                        bgShape(6,R.color.light_green)}.addOnClick {
                        ToastUtils.show("getwordDao")
//                (requireActivity().application as NewsApp).wordDb.getWordDao()
                    }
                }
                LinearLayout{
                    layout_height= match_parent
                    layout_width= wrap_content
                    orientation= vertical
                    bgShape(10, R.color.blue)

                    TextView { weight=1f
                        observe(queryId){
                            text=it.toString()
                        }
                    }.addOnClick {
                        XPopup.Builder(context).asInputConfirm("输入Id",""){
                            it.toLongOrNull()?.let {
                                queryId.value=it
                            }
                        }.show()
                    }
                    TextView{ weight=1f
                        text="queryById"
                    }.addOnClick {
                        observe(mainViewModel.queryById(queryId.value!!)){
                            Log.e("TAG", ": $it", )
                            it.toast
                        }

                    }
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allWord.observe(viewLifecycleOwner){
//            it.forEach { Log.e("TAG", it.toString()) }
            Log.e("TAG", "allWord observe-》: ${it.joinToString()}", )
        }
    }
}
val Any?.toast:String
    get(){
        return this.toString().also { ToastUtils.show(it) }
    }