package com.blcodes.mvp_framework.view.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.blcodes.mvp_framework.R

abstract class BaseDialog(context: Context, themeResId:Int = R.style.DialogFragment) : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        initView()
    }

    abstract fun initView()
    abstract fun initLayout(): Int
}