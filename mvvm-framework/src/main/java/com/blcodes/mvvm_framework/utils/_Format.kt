package com.blcodes.mvvm_framework.utils

import java.text.SimpleDateFormat
import java.util.*

fun bytes2HexString(bytes: ByteArray?, isUpperCase: Boolean): String {
    if (bytes == null || bytes.isEmpty()) {
        return ""
    }
    val hexDigits: CharArray = if (isUpperCase) {
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    } else {
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    }
    val len: Int = bytes.size
    val ret: CharArray = CharArray(len shl 1)
    var i = 0
    var j = 0
    while (i < len) {
        ret[j++] = hexDigits[bytes[i].toInt() shr 4 and 0x0f]
        ret[j++] = hexDigits[bytes[i].toInt() and 0x0f]
        i++
    }
    return String(ret)
}

fun toNormalTimeStr(time: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formatDest = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val date = format.parse(time)
    return formatDest.format(date)
}

fun toNormalTimeStr(time: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return format.format(Date(time))
}