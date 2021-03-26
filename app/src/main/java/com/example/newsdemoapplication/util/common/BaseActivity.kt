package com.example.newsdemoapplication.util.common

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.lishuaihua.materialdialogs.MaterialDialog
import java.lang.reflect.ParameterizedType


open abstract class BaseActivity<U : BaseViewModel> : AppCompatActivity() {
    protected lateinit var vm: U
        private set

    private val DEFAULT_STATUS_BAR_COLOR = Color.GRAY

    protected//获取status_bar_height资源的ID
    //根据资源ID获取响应的尺寸值
    val statusBarHeight: Int
        get() {
            var height = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                height = resources.getDimensionPixelSize(resourceId)
            }
            return height
        }
    private var layoutResId = 0

    // 是否设置成透明状态栏，即就是全屏模式
    protected fun isUseFullScreenMode(): Boolean {
        return true
    }


    private var dialog: MaterialDialog? = null

    protected open fun showLoading() {
//        showLoading(getString(R.string.loading))
    }

    protected open fun showLoading(meesage:String) {
//        if (dialog == null) {
//            var view = LayoutInflater.from(context).inflate(R.layout.layout_custom_progress_dialog_view, null)
//            view.findViewById<TextView>(R.id.tv_loading_message).setText(meesage)
//            dialog = this.let {
//                MaterialDialog(it)
//                        .cancelable(false)
//                        .cancelOnTouchOutside(false)
//                        .customView(null, view)
//                        .lifecycleOwner(this)
//            }
//        }
//        if (!isFinishing) {
//            dialog?.show()
//        }
    }

    protected open fun hideLoading() {
        if (!isFinishing) {
            dialog?.dismiss()
        }
    }

    override fun onDestroy() {
        if (!isFinishing) {
            dialog?.dismiss()
        }

        super.onDestroy()
    }


    // 在setContentView之前执行
    private fun setBaseStatusBar(color: Int) {
        /*
         为统一标题栏与状态栏的颜色，我们需要更改状态栏的颜色，而状态栏文字颜色是在android 6.0之后才可以进行更改
         所以统一在6.0之后进行文字状态栏的更改
        */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (isUseFullScreenMode()) {
//                SystemUtils.transparencyBar(this)
//            } else {
//                SystemUtils.setStatusBarColor(this, color)
//            }
//            SystemUtils.setLightStatusBar(this, false)
//        } else {
//            if (isUseFullScreenMode()) {
//                SystemUtils.transparencyBar(this)
//            } else {
//                SystemUtils.setStatusBarColor(this, color)
//            }
//            SystemUtils.setLightStatusBar(this, false)
//        }

    }

//    /**
//     * 设置沉浸状态栏
//     */
//    protected fun setImmersionBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val window = window
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = Color.TRANSPARENT
//        }
//    }
//
//    fun setAndroidNativeLightStatusBar(dark: Boolean) {
//        val decor = window.decorView
//        if (dark) {
//            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        } else {
//            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        }
//    }

    /**
     * Do create view business.
     *
     * @param savedInstanceState the saved instance state.
     */
    protected abstract fun doCreateView(savedInstanceState: Bundle?)

    /**
     * Get the layout resource id from subclass.
     *
     * @return layout resource id.
     */
    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    /**
     * This method will be called before the [.setContentView] was called.
     *
     * @param savedInstanceState the saved instance state.
     */
    protected open fun setupContentView(savedInstanceState: Bundle?) {
        setContentView(layoutResId)
    }

    /**
     * Initialize view model. Override this method to add your own implementation.
     *
     * @return the view model will be used.
     */
    protected fun createViewModel(): U {
        val vmClass: Class<U> = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<U>
        return ViewModelProvider(this, NewInstanceFactory()).get(vmClass)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initBase()
        super.onCreate(savedInstanceState)
        layoutResId = getLayoutResId()
        require(layoutResId > 0) { "The subclass must provider a valid layout resources id." }
        vm = createViewModel()
        setupContentView(savedInstanceState)
        doCreateView(savedInstanceState)
    }

    protected open fun initBase() {}


    /**
     * Correspond to fragment's [Fragment.getContext]
     *
     * @return context
     */
    protected val context: Context
        get() = this

    /**
     * Correspond to fragment's [Fragment.getActivity]
     *
     * @return activity
     */
    protected val activity: Activity
        get() = this


    /**
     * This method is used to call the super [.onBackPressed] instead of the
     * implementation of current activity. Since the current [.onBackPressed] may be override.
     */
    fun superOnBackPressed() {
        super.onBackPressed()
    }


}