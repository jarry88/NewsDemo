package com.example.newsdemoapplication.ui.main_activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.concurrent.fixedRateTimer

/**
 * 根Activity容器 ，后期全局添加 播放，横屏管控 ，数据统计等功能
 * 会在这个activity中统一调用
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(R.id.cl_container,HomeFragment())
                .commit()
    }
}