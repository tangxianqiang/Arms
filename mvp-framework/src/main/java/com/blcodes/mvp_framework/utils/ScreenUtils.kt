package com.blcodes.mvp_framework.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.blcodes.mvp_framework.model.data.Location
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
fun Activity.setStatusBarLightMode(isLightMode: Boolean) {
    transparentStatusBar()
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            window?.let {
                WindowCompat.getInsetsController(it, it.decorView)?.isAppearanceLightStatusBars = isLightMode
            }
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            val decorView: View = window.decorView
            var vis: Int = decorView.systemUiVisibility
            vis = if (isLightMode) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = vis;
        }
        else -> {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

}

@Suppress("DEPRECATION")
fun Activity.transparentStatusBar() {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            window?.let {
                //显示状态栏
                WindowCompat.getInsetsController(it, it.decorView)?.show(WindowInsetsCompat.Type.statusBars())
                WindowCompat.getInsetsController(it, it.decorView)?.hide(WindowInsetsCompat.Type.navigationBars())

                //透明状态栏
                window.statusBarColor = Color.TRANSPARENT
            }
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        else -> {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}

@Suppress("DEPRECATION")
fun Activity.setNavigationBarVisibility() {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            window?.let {
                WindowCompat.getInsetsController(it, it.decorView)?.hide(WindowInsetsCompat.Type.navigationBars())
            }
        }
        else -> {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}

/**
 * 屏幕加上虚拟按键的高度
 */
val Context.screenRealHeight: Int
    get() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)

        return displayMetrics.heightPixels
    }

/**
 * 屏幕高度
 */
val Context.screenHeight: Int
    get() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.heightPixels
    }

/**
 * 屏幕宽度
 */
val Context.screenWidth: Int
    get() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.widthPixels
    }

/**
 * 状态栏高度
 */
val Context.statusBarHeight: Int
    get() {
        val resourceId =
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")

        return Resources.getSystem().getDimensionPixelSize(resourceId)
    }

val Context.appBarHeight: Int
    get() {
        val typedArray = obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        val height = typedArray.getDimension(0, 0f)
        typedArray.recycle()

        return height.toInt()
    }

/**
 * 获取View在屏幕上的坐标
 */
val View.location: Location
    get() {
        val location = IntArray(2)
        getLocationOnScreen(location)

        return Location(location[0], location[1])
    }

fun Number.px2Dp(context: Context?) = if (context != null) {
    toFloat() / context.resources.displayMetrics.density
} else {
    0F
}

fun Number.dp2Px(context: Context?) = dp2PxFloat(context).roundToInt()

fun Number.dp2PxFloat(context: Context?) = if (context != null) {
    toFloat() * context.resources.displayMetrics.density
} else {
    0F
}

fun Number.px2Sp(context: Context?) = if (context != null) {
    toFloat() / context.resources.displayMetrics.scaledDensity
} else {
    0F
}

fun Number.sp2Px(context: Context?) = sp2PxFloat(context).roundToInt()

fun Number.sp2PxFloat(context: Context?) = if (context != null) {
    toFloat() * context.resources.displayMetrics.scaledDensity
} else {
    0F
}