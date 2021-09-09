package com.blcodes.mvp_framework.utils

import android.content.Context

fun Context.getCurrentPackageName(): String = try {
    val packageManager = this.packageManager
    val packageInfo = packageManager.getPackageInfo(
            this.packageName, 0)
    packageInfo.packageName
} catch (e: Exception) {
    ""
}
