package com.blcodes.mvvm_framework.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * 判断String是否为Base64
 */
fun String?.isBase64(): Boolean {
    if (this != null && startsWith("data:image/")) return true

    return false
}

/**
 * Base64转bitmap
 * 耗时操作，必须在协程中调用
 */
suspend fun String?.base64ToBitmap(): Bitmap? {
    if (!this.isBase64()) return null

    return withContext(Dispatchers.IO) {
        val matcher: Matcher = Pattern.compile(",").matcher(this@base64ToBitmap)
        var index = 0

        if (matcher.find()) {
            index = matcher.start()
        }
        val decode = Base64.decode(this@base64ToBitmap?.substring(index ?: 0), Base64.DEFAULT)

        BitmapFactory.decodeByteArray(decode, 0, decode.size)
    }
}

/**
 * 主要用于DataBinding中代替toString，避免空值带来的崩溃
 */
fun Any?.anyString() = this?.toString() ?: ""