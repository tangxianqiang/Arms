package com.blcodes.mvvm_framework.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

class CoroutineLifecycleListener(private val deferred: Deferred<*>) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancelCoroutine() {
        if (!deferred.isCancelled) {
            deferred.cancel()
        }
    }
}

/**
 * load...then...提供快速异步回调处理机制，该机制确定了协程的生命周期是和viewModel无关的，只与activity有关
 */
fun <T> LifecycleOwner.load(loader: () -> T): Deferred<T> {
    val deferred = CoroutineScope(context = Dispatchers.IO).async(start = CoroutineStart.LAZY) {
        loader()
    }

    lifecycle.addObserver(CoroutineLifecycleListener(deferred))
    return deferred
}

infix fun <T> Deferred<T>.then(block: (T) -> Unit): Job {
    return CoroutineScope(Dispatchers.Main).launch {
        block(this@then.await())
    }
}


