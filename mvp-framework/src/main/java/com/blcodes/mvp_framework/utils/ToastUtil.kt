package com.blcodes.mvp_framework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.blcodes.mvp_framework.R

private var toast: Toast? = null

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (text == null) return
    if (Thread.currentThread() !== Looper.getMainLooper().thread) return

    toast?.cancel()
    toast = Toast(this).apply {
        view = (LayoutInflater.from(this@showToast)
            .inflate(R.layout.toast_black, null) as TextView).apply {
            this.text = text
        }
        this.duration = duration
        setGravity(Gravity.BOTTOM and Gravity.CENTER_HORIZONTAL, 0, 0)

        show()
    }
}

fun Context.showToast(@StringRes res: Int) {
    showToast(getString(res))
}

fun Fragment.showToast(text: String?) {
    context?.showToast(text)
}

fun Fragment.showToast(@StringRes res: Int) {
    context?.showToast(res)
}