package com.blcodes.mvp_framework.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blcodes.mvp_framework.presenter.contract.IBaseContract
import com.blcodes.mvp_framework.utils.setStatusBarLightMode

abstract class BasePresenterActivity<V:IBaseContract.IBaseView,P : IBaseContract.IBasePresenter<V>> :
    AppCompatActivity(), IBaseContract.IBaseView {

    protected var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()
        setContentView(initResLayout())

        mPresenter = initPresenter()
        mPresenter?.let {
            lifecycle.addObserver(it)
            it.attachView(this as V)
        }

        //change or init view in activity after "findViewById"
        initView()
    }

    abstract fun initPresenter(): P

    abstract fun initView()

    abstract fun initResLayout(): Int

    protected fun initWindow() {
        //TODO 所有Activity应该具有的窗体样式、全局设置、字体、软键盘等应该在这里设置，如果有不一样的窗体样式，则覆盖这个方法
        //这里给出一个常用的白色状态栏模式
        setStatusBarLightMode(true)
    }

}