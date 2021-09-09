package com.blcodes.mvvm_framework.utils

import android.view.View

abstract class OnSingleClickListener(var delay: Int? = MIN_CLICK_DELAY) :
    View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View?) {
        val current = System.currentTimeMillis()

        if (current - lastClickTime > (delay ?: MIN_CLICK_DELAY)) {
            lastClickTime = current
            onSingleClick(v)
        }
    }

    /**
     *  如果要手动使用该类的话，必须实现该方法而不是onClick()方法
     */
    abstract fun onSingleClick(view: View?)

    companion object {
        private const val MIN_CLICK_DELAY = 500
    }
}