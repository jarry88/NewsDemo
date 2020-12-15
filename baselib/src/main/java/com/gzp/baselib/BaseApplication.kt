package com.gzp.baselib

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.webkit.WebView
import com.gzp.baselib.constant.Constants
import com.gzp.baselib.utils.DevUtils
import com.lishuaihua.toast.ToastUtils
import okhttp3.OkHttpClient
import com.gzp.baselib.utils.UtilsApp
import org.tianguang.baselib.utils.device.DeviceIdUtil
import org.tianguang.baselib.utils.device.MD5Util
import java.util.*


open class BaseApplication : Application(), Application.ActivityLifecycleCallbacks {
    private var activityMap = HashMap<String, Activity>()
    override fun onCreate() {
        super.onCreate()
//        MultiDex.install(applicationContext)
//        Bugly.init(applicationContext, Constants.BUGLY_APP_ID, BuildConfig.DEBUG)
//        //获取fcm pushtoken
//        getFcmPushToken()
//        //初始化风控SDK
//        HttpContext.setContext(this)
//        YunContext.setContext(this.applicationContext)
//        YunHttpManager.setInterfaceAddress("https://test-sdk-data-colleciton.cashok.in/")
//        YunMain.setDebug(BuildConfig.DEBUG)

        instance = this
        UtilsApp.init(this)
        ToastUtils.init(this)
        //数据库初始化
//        DataManager.init(this)
//        //活体检测SDK 初始化
//        GuardianLivenessDetectionSDK.init(this, BuildConfig.LivenessAccessKey, BuildConfig.livenessSecretKey, Market.India)
        val macAddr = DevUtils.getMac(this)
        var devToken= DevUtils.getDeviceId(this)+macAddr
        var androidId: String = MD5Util.md5(DeviceIdUtil.getAndroidId(this))
        val builder = OkHttpClient.Builder()
        builder.addInterceptor { chain ->
            val request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .addHeader("Accept", "application/json")
//                    .addHeader("appversion", BuildConfig.VERSION_NAME)
                    .addHeader("accept-language", "en-US;hi-IN")
                    .addHeader("mobileType", Build.PRODUCT)
                    .addHeader("clientType", "ANDROID")
                    .addHeader("IMEI", androidId)
                    .addHeader("macAddr", macAddr)
                    .addHeader("devToken", devToken)
                    .addHeader("appId", Constants.APPID)
                    .addHeader("channelCode", Constants.CHANNEL_CODE)
                    .addHeader("brand", Build.BRAND)
                    .addHeader("appPackageName", Constants.APP_PACKAGENAME)
                    .build()
            chain.proceed(request)
        }
//        RetrofitClient.init(applicationContext, BuildConfig.BASEURL, 0, builder, BuildConfig.DEBUG)
//        UpLaodRetrofitClient.init(applicationContext, BuildConfig.BASEURL, 0, builder, BuildConfig.DEBUG)
//
//
//        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
//            ARouter.openLog()     // 打印日志
//            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        }
//        ARouter.init(this)
        registerActivityLifecycleCallbacks(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName(this)
            val packageName = this.packageName
            if (packageName != processName) {
                WebView.setDataDirectorySuffix(processName!!)
            }
        }
    }

//    private fun getFcmPushToken() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//            // Log and toast
//            Log.d("TAG", "pushtoken --->$token")
//
//            SharedPreferencesManager.getInstance().saveString(SharedPreferencesManager.PUSH_TOKEN, token)
//
//        })
//
//    }
    fun init(){}

    open fun getProcessName(context: Context): String? {
         var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityMap[activity.hashCode().toString() + ""] = activity
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {


    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }


    override fun onActivityDestroyed(activity: Activity) {
    }


    fun exitApp() {
        for (activity in activityMap.values) {
            activity.finish()
        }
    }

    companion object {
        private val TAG = BaseApplication::class.java.simpleName

        @get:Synchronized
        lateinit var instance: BaseApplication

        //static 代码段可以防止内存泄露
        init {
//            //设置全局的Header构建器
//            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//                ClassicsHeader(context)
//            }
//            //设置全局的Footer构建器
//            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
//                ClassicsFooter(context)
//            }
        }
    }

}
