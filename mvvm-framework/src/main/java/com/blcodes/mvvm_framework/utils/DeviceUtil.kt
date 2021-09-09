package com.blcodes.mvvm_framework.utils

import android.os.Build
import java.util.*

const val ROM_BRAND_UNKNOWN = "unknown"
val MOBYDATA = arrayOf("mobydata", "mobydata")
val NEOLIX = arrayOf("neolix", "neolix")
val ZPD = arrayOf("android", "zpd")
val UNKNOWN_ROM = arrayOf("-", "-")

/**
 * 获取终端系统定制商
 */
fun getRomBrand(): String = try {
    Build.BRAND.toLowerCase(Locale.getDefault())
} catch (e: Exception) {
    ROM_BRAND_UNKNOWN
}

/**
 * 获取终端制造商
 */
fun getRomManufacturer(): String = try {
    Build.MANUFACTURER.toLowerCase(Locale.getDefault())
} catch (e: Exception) {
    ROM_BRAND_UNKNOWN
}

/**
 * 获取终端对应厂家型号
 */
fun getRomModel(): String = try {
    Build.MODEL.toLowerCase(Locale.getDefault())
} catch (e: Exception) {
    ROM_BRAND_UNKNOWN
}

fun Array<String>.isTheCurrentRom(): Boolean = if (this.size < 2) {
    false
} else {
    getRomBrand() == this[0] && getRomManufacturer() == this[1]
}