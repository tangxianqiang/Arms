package com.blcodes.mvvm_framework.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

inline fun <reified T : Activity> Context.startActivityEasy(
    autoFinish: Boolean = false,
    flags: Int? = null,
    block: Intent.() -> Unit
) {
    startActivity(Intent(this, T::class.java).apply {
        block()
        flags?.let { this.flags = it }
    })

    if (autoFinish && this is Activity) {
        finish()
    }
}

/**
 * 在Fragment中调用
 */
inline fun <reified T : Activity> Fragment.startActivityEasy(
    autoFinish: Boolean = false,
    flags: Int? = null,
    block: Intent.() -> Unit
) {
    context?.startActivityEasy<T>(flags = flags, block = block)

    if (autoFinish) {
        activity?.finish()
    }
}

/**
 * 关闭软键盘
 */
fun Activity.closeKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
    window.decorView.requestFocus()
}

/**
 * 手动打开软键盘
 */
fun Activity.openKeyboard(et: EditText) {
    et.requestFocus()
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }
}

/**
 * 关闭软键盘
 */
fun Fragment.closeKeyboard() {
    activity?.closeKeyboard()
}

/**
 * 手动打开软键盘
 */
fun Fragment.openKeyboard(et: EditText) {
    activity?.openKeyboard(et)
}