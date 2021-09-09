package com.blcodes.mvp_framework.extensions

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

    abstract fun onSingleClick(view: View?)

    companion object {
        private const val MIN_CLICK_DELAY = 500
    }
}

fun View.setOnSingleClickListener(action:(v:View)->Unit){
    this.setOnClickListener(object :OnSingleClickListener(){
        override fun onSingleClick(view: View?) {
            view?.let {
                action.invoke(view)
            }
        }

    })
}