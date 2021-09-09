package com.blcodes.mvp_framework.presenter

import com.blcodes.mvp_framework.presenter.contract.IBaseContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class RxPresenter<V : IBaseContract.IBaseView> : BasePresenter<V>() {
    private val TAG = "RxPresenter"

    //观察者订阅管理对象
    protected var compositeDisposable: CompositeDisposable? = null

    //订阅者
    private var disposable: Disposable? = null

    /**
     * 取消所有订阅
     */
    protected fun unSubscribe() {
        compositeDisposable?.clear()
    }

    /**
     * 添加订阅
     * @param subscription
     */
    protected fun addSubscribe(subscription: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        disposable = subscription
        compositeDisposable?.add(subscription)
    }

    override fun detachView() {
        super.detachView()
        //一个Presenter管理一个订阅集合
        unSubscribe()
    }

    fun cancelCurrent() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}