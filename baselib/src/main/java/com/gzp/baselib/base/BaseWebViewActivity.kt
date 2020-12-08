package com.gzp.baselib.base

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.gzp.baselib.R
import com.gzp.baselib.base.BaseViewModel
import com.gzp.baselib.constant.Constants
import com.gzp.baselib.databinding.ActivityBaseWebViewBinding
import com.gzp.baselib.widget.NavigationBar

@Route(path = Constants.BaseWebViewPath)
class BaseWebViewActivity : MvvmBaseActivity<BaseViewModel, ActivityBaseWebViewBinding>() {

    override fun getLayoutResId(): Int = R.layout.activity_base_web_view
    override fun doCreateView(savedInstanceState: Bundle?) {
        val navigationBar = binding.baseWebviewNavigationBar as NavigationBar
        navigationBar.setBackClickListener(this)
        val bundle = intent.extras
        var title = bundle?.getString("title")
        var url = bundle?.getString("url")
        if (!TextUtils.isEmpty(title)){
            binding.baseWebviewNavigationBar.visibility=View.VISIBLE
            navigationBar.setTitle(title!!)
        }else{
            binding.baseWebviewNavigationBar.visibility=View.GONE
        }
        if (!TextUtils.isEmpty(url)){
            binding.webView.loadUrl(url!!)
        }

        initWebView()
    }

    private fun initWebView() {
        val webSettings = binding.webView.settings
        webSettings.domStorageEnabled = true//主要是这句
        webSettings.javaScriptEnabled = true//启用js
        webSettings.blockNetworkImage = false//解决图片不显示
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.setAppCacheEnabled(false)
        WebView.setWebContentsDebuggingEnabled(true);
        binding.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        binding.webView.setWebChromeClient(object :WebChromeClient(){
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
               binding. progressBar.progress = newProgress
                if(newProgress == 100) binding. progressBar.visibility = View.GONE
            }
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }
            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }
        })
        //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

    }


}