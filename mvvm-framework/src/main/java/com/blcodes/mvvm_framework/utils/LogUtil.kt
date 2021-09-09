package com.blcodes.mvvm_framework.utils

import android.util.Log

private const val TAG_I = "Info------------------>"
private const val TAG_E = "Error----------------->"
private const val TAG_D = "Debug----------------->"
private const val TAG_W = "Warning--------------->"

fun Any?.logInfo(tag: String? = null) {
    Log.i(tag ?: TAG_I, getString(this))
}

fun Any?.logDebug(tag: String? = null) {
    Log.d(tag ?: TAG_D, getString(this))
}

fun Any?.logError(tag: String? = null) {
    Log.e(tag ?: TAG_E, getString(this))
}

fun Any?.logWarn(tag: String? = null) {
    Log.w(tag ?: TAG_W, getString(this))
}

private fun getString(any: Any?) = when (any) {
    null -> {
        "null"
    }
    is Throwable -> {
        any.stackTraceToString()
    }
    else -> {
        "$any"
    }
}