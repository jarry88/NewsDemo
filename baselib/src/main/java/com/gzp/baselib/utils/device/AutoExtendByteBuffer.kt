package org.tianguang.baselib.utils.device

import java.nio.ByteBuffer

class AutoExtendByteBuffer(initSize: Int) {
    private var mBuffer: ByteBuffer
    private fun ensureSize(allocate: Int) {
        val position = mBuffer.position()
        var capacity = mBuffer.capacity()
        val newPosition = allocate + position
        if (newPosition <= capacity) {
            return
        }
        while (newPosition > capacity) {
            capacity = capacity shl 1
        }
        val newBuffer = ByteBuffer.allocate(capacity)
        mBuffer.flip()
        newBuffer.put(mBuffer)
        mBuffer = newBuffer
    }

    fun putInt(value: Int) {
        ensureSize(4)
        mBuffer.putInt(value)
    }

    fun putFloat(value: Float) {
        ensureSize(4)
        mBuffer.putFloat(value)
    }

    fun putString(value: String?) {
        if (value == null || value.isEmpty()) {
            return
        }
        val bytes = value.toByteArray()
        ensureSize(bytes.size)
        mBuffer.put(bytes)
    }

    fun array(): ByteArray {
        return mBuffer.array()
    }

    fun position(): Int {
        return mBuffer.position()
    }

    val longHash: Long
        get() = MHash.hash64(mBuffer.array(), mBuffer.position())

    init {
        mBuffer = ByteBuffer.allocate(if (initSize > 16) initSize else 16)
    }
}