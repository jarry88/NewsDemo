package org.tianguang.baselib.utils.device

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import java.net.NetworkInterface
import kotlin.experimental.and

object DeviceIdUtil {
    private val HEX_DIGITS = byteArrayOf(
        '0'.toByte(),
        '1'.toByte(),
        '2'.toByte(),
        '3'.toByte(),
        '4'.toByte(),
        '5'.toByte(),
        '6'.toByte(),
        '7'.toByte(),
        '8'.toByte(),
        '9'.toByte(),
        'a'.toByte(),
        'b'.toByte(),
        'c'.toByte(),
        'd'.toByte(),
        'e'.toByte(),
        'f'.toByte()
    )
    private const val INVALID_MAC_ADDRESS = "02:00:00:00:00:00"
    private const val INVALID_ANDROID_ID = "9774d56d682e549c"
    private val macInArray: ByteArray?
        private get() {
            try {
                val enumeration = NetworkInterface.getNetworkInterfaces() ?: return null
                while (enumeration.hasMoreElements()) {
                    val netInterface = enumeration.nextElement()
                    if (netInterface.name == "wlan0") {
                        return netInterface.hardwareAddress
                    }
                }
            } catch (e: Exception) {
                Log.e("tag", e.message, e)
            }
            return null
        }
    val longMac: Long
        get() {
            val bytes = macInArray
            if (bytes == null || bytes.size != 6) {
                return 0L
            }
            var mac = 0L
            for (i in 0..5) {
                mac = mac or (bytes[i].toLong() and 0xFF)
                if (i != 5) {
                    mac = mac shl 8
                }
            }
            return mac
        }
    val macAddress: String
        get() {
            val mac = formatMac(macInArray)
            return if (TextUtils.isEmpty(mac) || mac == INVALID_MAC_ADDRESS) {
                ""
            } else mac
        }

    private fun formatMac(bytes: ByteArray?): String {
        if (bytes == null || bytes.size != 6) {
            return ""
        }
        val mac = ByteArray(17)
        var p = 0
        for (i in 0..5) {
            val b = bytes[i]
            mac[p] = HEX_DIGITS[b.toInt() shr 4 and 0xF]
            mac[p + 1] = HEX_DIGITS[b.toInt() and 0xF]
            if (i != 5) {
                mac[p + 2] = ':'.toByte()
                p += 3
            }
        }
        return String(mac)
    }

    fun getAndroidId(context: Context): String {
        @SuppressLint("HardwareIds") val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        if (!TextUtils.isEmpty(androidId) && INVALID_ANDROID_ID != androidId) {
            return androidId
        }
        return ""
    }

    /**
     * TODO 这个方法有问题
     */
    @SuppressLint("MissingPermission")
    fun getDeviceId(context: Context): String {
        var deviceId: String = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            deviceId =
                (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceSoftwareVersion!!
        } else {
            deviceId =
                (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).getDeviceId()
        }
        return deviceId
    }
}