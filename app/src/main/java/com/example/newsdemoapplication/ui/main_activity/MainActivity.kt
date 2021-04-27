package com.example.newsdemoapplication.ui.main_activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.newsdemoapplication.BuildConfig
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.databinding.ActivityMainBinding
import com.example.newsdemoapplication.ui.BlackFragment
import com.example.newsdemoapplication.ui.home.HomeFragment
import com.example.newsdemoapplication.ui.manager.AddSectionFragment
import me.yokeyword.fragmentation.Fragmentation
import me.yokeyword.fragmentation.SupportActivity

/**
 * 根Activity容器 ，后期全局添加 播放，横屏管控 ，数据统计等功能
 * 会在这个activity中统一调用
 */
class MainActivity : SupportActivity() {

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
        binding
//        binding.zuoyeId.apply {
//            tjanjiadidianAn.setOnClickListener {
//                closeDrawerLayout()
//
//            }
//            zuoyeGuanbiAn.setOnClickListener { closeDrawerLayout()}
//            updateButton.setOnClickListener { homeFragment.refreshDate()}
//        }
        loadRootFragment(R.id.ft_container,homeFragment)

        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install()

    }
    fun closeDrawerLayout(left:Boolean=true){
        mDrawerLayout.closeDrawer(if(left)GravityCompat.START else GravityCompat.END)
    }
}