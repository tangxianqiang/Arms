package com.blcodes.mvvm_framework.utils

import android.util.Log
import com.blcodes.mvvm_framework.BuildConfig.DEBUG
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object ResponseFormatUtil {
    private val LINE_SEPARATOR = System.getProperty("line.separator")

    @JvmStatic
    fun printJson(msg: String) {
        if (!DEBUG) return

        var message = try {
            when {
                msg.startsWith("{") -> {
                    val jsonObject = JSONObject(msg)
                    //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                    jsonObject.toString(4)
                }
                msg.startsWith("[") -> {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(4)
                }
                else -> {
                    msg
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()

            msg
        }
        printLine(true)
        message = LINE_SEPARATOR + message
        val lines = message.split(LINE_SEPARATOR?.toRegex() ?: "".toRegex()).toTypedArray()
        for (line in lines) {
            Log.i("", "║ $line")
        }

        printLine(false)
    }

    fun printLine(isTop: Boolean) {
        if (isTop) {
            Log.i(
                "",
                "╔═══════════════════════════════════════════════════════════════════════════════════════"
            )
        } else {
            Log.i(
                "",
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
        }
    }
}