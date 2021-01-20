package com.example.newsdemoapplication.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.newsdemoapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity:AppCompatActivity() {
    lateinit var viewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
        viewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_test)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController)
    }
}