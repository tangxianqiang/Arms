package com.blcodes.mvvm_framework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.blcodes.mvvm_framework.R

private var toast: Toast? = null

/**
 * 采用Context的扩展，强制限定toast必须在视图中被调用
 */
@SuppressLint("InflateParams", "UseCompatLoadingForDrawables")
fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT, @DrawableRes img: Int = R.drawable.icon_notice) {
    if (text == null) return
    if (Thread.currentThread() !== Looper.getMainLooper().thread) return

    toast?.cancel()
    toast = Toast(this).apply {
        view = (LayoutInflater.from(this@showToast).inflate(R.layout.toast_black, null) as LinearLayout).apply {
            findViewById<TextView>(R.id.tv_toast_content).text = text
            findViewById<ImageView>(R.id.tv_toast_img).background = getDrawable(img)
        }
        this.duration = duration
        setGravity(Gravity.CENTER, 0, 0)

        show()
    }
}

fun Context.showToast(@StringRes res: Int, @DrawableRes img: Int = R.drawable.icon_notice) {
    showToast(getString(res), img = img)
}

fun Fragment.showToast(text: String?, @DrawableRes img: Int = R.drawable.icon_notice) {
    context?.showToast(text,img = img)
}

fun Fragment.showToast(@StringRes res: Int, @DrawableRes img: Int = R.drawable.icon_notice) {
    context?.showToast(res, img = img)
}