package com.blcodes.mvvm_framework.view

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blcodes.mvvm_framework.BaseException
import com.blcodes.mvvm_framework.ERROR_TYPE_NULL
import com.blcodes.mvvm_framework.ERROR_TYPE_NULL_LAYOUT
import com.blcodes.mvvm_framework.utils.setStatusBarLightMode
import com.blcodes.mvvm_framework.utils.showToast
import com.blcodes.mvvm_framework.vm.BaseViewModel

/**
 *
 * 基础Activity负责基础UI控制、权限、部分常用的逻辑控制
 * 应该具备的能力
 * 1、控制状态栏、导航栏、主题、软件盘等
 * 2、负责初始化root view
 * 3、处理liveData的数据，有时候需要监听数据变化，操作一些逻辑
 * 4、公共错误处理
 */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    private lateinit var binding: T
    protected val viewModel by viewModels<BaseViewModel>()

    /*是否需要关闭软甲盘*/
    var needCloseSoftWin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()
        binding = DataBindingUtil.setContentView<T>(
            this,
            if (initResLayout() == 0) getBaseFail() else initResLayout()
        ).apply {
            lifecycleOwner = this@BaseActivity
        }
        //change or init view in activity after "findViewById"
        initView()
    }

    open fun initView() {
        //初始化binding的数据
        //调整布局ui
    }

    open fun onClick(v: View) {
        //布局点击事件
    }

    abstract fun initResLayout(): Int

    open fun onObserve() {

    }

    open fun doWhileError(){
        //默认的错误处理，可以自行覆写
        bindError()
    }

    private fun bindError(){
        showToast(viewModel.baseError.value?.message?:ERROR_TYPE_NULL)
    }

    open fun initWindow() {
        //所有Activity应该具有的窗体样式、全局设置、字体、软键盘等应该在这里设置，如果有不一样的窗体样式，则覆盖这个方法
        //这里给出一个常用的白色状态栏模式
        setStatusBarLightMode(true)
    }

    /**
     * 设置点击activity时，软件盘是否关闭
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP && needCloseSoftWin) {
            if (isShouldHideKeyboard(currentFocus, ev)) {
                hideSoftKeyboard(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 关闭软件盘
     */
    open fun hideSoftKeyboard(activity: Activity) {
        val im = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity.currentFocus != null) {
            im.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    /**
     * 针对输入框，判断是否需要关闭软件盘
     */
    protected fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v is EditText) {
            val vLocation = intArrayOf(0, 0)
            v.getLocationOnScreen(vLocation)
            val left = vLocation[0]
            val top = vLocation[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.rawX > left && event.rawX < right
                    && event.rawY > top && event.rawY < bottom)
        }
        return false
    }

    open fun getBaseFail(): Nothing {
        if (initResLayout() == 0) {
            throw BaseException(ERROR_TYPE_NULL_LAYOUT)
        } else throw IllegalAccessException()
    }
}