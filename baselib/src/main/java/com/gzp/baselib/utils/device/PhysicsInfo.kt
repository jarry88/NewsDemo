package org.tianguang.baselib.utils.device

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import java.io.BufferedReader
import java.io.File
import java.io.FileFilter
import java.io.FileReader

object PhysicsInfo {
    private const val TAG = "PhysicsInfo"
    fun getHash(context: Context): Long {
        val totalInfo = (Build.MODEL + ","
                + cPUCores + ","
                + getRamSize(context) + ","
                + romSize + ","
                + getWindowInfo(context))
        return MHash.hash64(totalInfo)
    }

    val cPUCores: Int
        get() {
            val cores: Int
            cores = try {
                File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).size
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                Runtime.getRuntime().availableProcessors()
            }
            return cores
        }
    private val CPU_FILTER = FileFilter { pathname ->
        val path = pathname.name
        if (path.startsWith("cpu")) {
            for (i in 3 until path.length) {
                if (path[i] < '0' || path[i] > '9') {
                    return@FileFilter false
                }
            }
            return@FileFilter true
        }
        false
    }

    // 从1G开始， 逐步试探，直到大于totalBytes
    val romSize: Int
        get() {
            val totalBytes = StatFs(Environment.getDataDirectory().path).totalBytes
            // 从1G开始， 逐步试探，直到大于totalBytes
            var size = (1 shl 30.toLong().toInt()).toLong()
            while (totalBytes > size) {
                size = size shl 1
            }
            return (size shr 30).toInt()
        }

    // 规范化为G, 因为精确到B的话变化的概率较大。
    private fun normalizeToG(size: Long): Int {
        return (size shr 30).toInt() + if (size and 0x3FFFFFFF == 0L) 0 else 1
    }

    fun getRamSize(context: Context): Int {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        return if (am != null) {
            am.getMemoryInfo(memoryInfo)
            normalizeToG(memoryInfo.totalMem)
        } else {
            normalizeToG(totalMemorySize)
        }
    }

    val totalMemorySize: Long
        get() {
            val dir = "/proc/meminfo"
            try {
                val fr = FileReader(dir)
                val br = BufferedReader(fr, 2048)
                val memoryLine = br.readLine()
                val subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"))
                br.close()
                return subMemoryLine.replace("\\D+".toRegex(), "").toInt() * 1024L
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
            return 0L
        }

    fun getWindowInfo(context: Context): String {
        val dm: DisplayMetrics
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (wm != null) {
            dm = DisplayMetrics()
            wm.defaultDisplay.getRealMetrics(dm)
        } else {
            dm = context.resources.displayMetrics
        }
        return dm.widthPixels.toString() + "x" + dm.heightPixels + " " + dm.xdpi + "x" + dm.ydpi
    }
}