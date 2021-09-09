package com.blcodes.mvp_framework.presenter.contract

import androidx.lifecycle.LifecycleObserver

interface IBaseContract {
    interface IBaseView {

    }

    interface IBasePresenter<in V> : LifecycleObserver {
        fun attachView(view: V)
        fun detachView()
    }
}