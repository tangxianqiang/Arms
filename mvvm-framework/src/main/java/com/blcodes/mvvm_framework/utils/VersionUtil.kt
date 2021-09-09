package com.blcodes.mvvm_framework.utils

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat


/**
 * 获取版本名称
 */
val Context.versionName: String
    get() = packageManager.getPackageInfo(packageName, 0).versionName

/**
 * 获取版本号
 */
val Context.versionCode: Long
    get() = PackageInfoCompat.getLongVersionCode(packageManager.getPackageInfo(packageName, 0))