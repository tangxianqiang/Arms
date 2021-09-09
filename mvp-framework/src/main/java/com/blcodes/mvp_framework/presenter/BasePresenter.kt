package com.blcodes.mvp_framework.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.blcodes.mvp_framework.presenter.contract.IBaseContract

abstract class BasePresenter<V : IBaseContract.IBaseView> : IBaseContract.IBasePresenter<V> {
    protected var view: V? = null
    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected fun onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected fun onDestroy() {
        this.detachView()
    }
}