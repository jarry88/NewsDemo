package com.gzp.baselib

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.webkit.WebView
import androidx.room.Room
import com.gzp.baselib.constant.Constants
import com.gzp.baselib.utils.DevUtils
import com.gzp.baselib.utils.UtilsApp
import com.lishuaihua.toast.ToastUtils
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
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

        instance = this
        val rootDir = MMKV.initialize(this)
        println("mmkv root: $rootDir")
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
        }
    }

}
