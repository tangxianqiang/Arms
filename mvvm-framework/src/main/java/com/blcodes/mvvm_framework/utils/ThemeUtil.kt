package com.blcodes.mvvm_framework.utils

import android.app.Activity
import android.view.View

object ThemeUtil {
    /**
     * 切换状态栏字体为黑色
     */
    @JvmStatic
    fun enableDarkStatus(activity: Activity?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.apply {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    /**
     * 切换状态栏字体为白色
     */
    @JvmStatic
    fun enableLightStatus(activity: Activity?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.apply {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }
}