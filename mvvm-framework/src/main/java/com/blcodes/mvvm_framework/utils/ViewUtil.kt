package com.blcodes.mvvm_framework.utils

import android.view.View

/**
 * 手动调用该方法时可用lambda表达式
 */
fun View.setOnSingleClickListener(delay: Int? = null, block: (View?) -> Unit) {
    setOnClickListener(object : OnSingleClickListener(delay) {
        override fun onSingleClick(view: View?) {
            block(view)
        }
    })
}

/**
 * 将普通的OnClickListener转化为OnSingleClickListener
 */
fun View.OnClickListener.toSingle() = object : OnSingleClickListener() {
    override fun onSingleClick(view: View?) {
        this@toSingle.onClick(view)
    }
}