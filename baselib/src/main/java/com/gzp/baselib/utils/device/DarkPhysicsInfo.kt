package org.tianguang.baselib.utils.device

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import java.net.NetworkInterface

object DarkPhysicsInfo {
    fun getHash(context: Context): Long {
        return getSensorHash(context) xor MHash.hash64(networkInterfaceNames)
    }

    private fun getSensorHash(context: Context): Long {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            ?: return 0L
        val buffer = AutoExtendByteBuffer(1024)
        val list = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in list) {
            buffer.putString(sensor.name)
            buffer.putString(sensor.vendor)
            buffer.putInt(sensor.version)
            buffer.putInt(sensor.type)
            buffer.putFloat(sensor.maximumRange)
            buffer.putFloat(sensor.resolution)
            buffer.putFloat(sensor.power)
            buffer.putInt(sensor.minDelay)
        }
        return MHash.hash64(buffer.array(), buffer.position())
    }

    private val networkInterfaceNames: String
        private get() {
            try {
                val enumeration = NetworkInterface.getNetworkInterfaces() ?: return ""
                val builder = StringBuilder(1024)
                while (enumeration.hasMoreElements()) {
                    val netInterface = enumeration.nextElement()
                    builder.append(netInterface.name).append(',')
                }
                if (builder.length > 0) {
                    builder.setLength(builder.length - 1)
                }
                return builder.toString()
            } catch (e: Exception) {
                Log.e("tag", e.message, e)
            }
            return ""
        }
}