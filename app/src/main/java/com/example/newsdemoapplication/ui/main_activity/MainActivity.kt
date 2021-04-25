package com.example.newsdemoapplication.ui.main_activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.ActivityMainBinding
import com.example.newsdemoapplication.ui.home.HomeFragment

/**
 * 根Activity容器 ，后期全局添加 播放，横屏管控 ，数据统计等功能
 * 会在这个activity中统一调用
 */
class MainActivity : AppCompatActivity() {

    //用按钮打开/关闭左右侧滑页//////////////////
    val mDrawerLayout by lazy {
        (findViewById<View>(R.id.drawer_layout) as DrawerLayout).also {
            it.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }
    val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
    }
    val homeFragment by lazy { HomeFragment() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.zuoyeId.zuoyeGuanbiAn.setOnClickListener { mDrawerLayout.closeDrawer(GravityCompat.START)}
        binding.zuoyeId.updateButton.setOnClickListener { homeFragment.refreshDate()}
        updateLeftList()
        supportFragmentManager.beginTransaction()
                .replace(R.id.cl_container,homeFragment)
                .commit()
    }

    fun updateLeftList(){
    }
}