package com.blcodes.mvvm_framework.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blcodes.mvvm_framework.BaseException
import kotlinx.coroutines.*

/**
 * 基本逻辑管理类
 * 包含的功能：
 * 1、出错状态保存，view层负责监听该状态
 * 2、使用协程发起数据请求：viewModelScope.launch开启协程，使用withContext切换线程，使用suspendCoroutine
 *    阻塞协程返回统一处理的回调结果。
 *    部分需要被压的地方：如监听输入框数据，当内容变化时马上请求网络，得到网络更新界面。此时输入框的内容变化很快，
 *    发起的请求非常多，而网络回调处理往往更不上发起请求的动作。在观察者模式中，肯定会存在观察者处理不过来被观察者
 *    发送事件的场景，就像快速点击按钮N次，真正响应的可能远小于N次，或者点击N次之后的长时间内，响应都还在执行。
 *    RxJava使用的背压：使用了一个无限大的缓冲池存放发送给观察者的事件，当事件过多，观察者处理不过来，事件就被缓冲，
 *    导致最后OOM或者其他问题。
 *    RxJava解决办法：使用背压相关的被观察者，假如缓存128个事件，多余的要么丢弃，要么抛异常
 *    协程的解决办法：FLow流，自己处理事件，直接抛弃
 * 3、数据恢复：vm自带数据恢复功能，activity重建的时候会自动恢复数据，不需要onSaveInstanceState来手动恢复，并且很少量，
 *    vm恢复全量的数据。
 * 4、在多并发的场景，可以使用async，如果需要对异常马上处理，停止所有工作，可以使用[awaitAll]
 */
class BaseViewModel : ViewModel() {
    /*直接由LiveData管理的数据，并且受限于View的生命周期，仅仅在切换到指定状态才观察接收变化，如果不受限于生命周期，使用observeForever*/
    val baseError = MutableLiveData<BaseException>()
    /*当前持有的协程，保存在VM中，控制协程的随时取消*/
    private var currentJob :Job? = null

    //以下是使用flow流的demo，处理背压的解决办法是：至少等600ms才处理一次flow流的事件，多余的排队，超过缓存则丢弃
    //使用一个热流发送搜索的数据，而不是冷流flow，冷流只有观察者订阅的时候，才会触发发送数据，这会导致不能发射搜索数据
//    val searchStateFlow = MutableStateFlow("")
//
//    @FlowPreview
//    fun stateFlowStartLaunch(){
//        viewModelScope.launch {
//            searchStateFlow
//                .sample(600)
//                .filter { it.isNotEmpty() }
//                .collect {
//                    //订阅数据
//                }
//        }
//    }

    /**
     * 使用协程获取数据
     * @param block     该闭包中执行具体的逻辑，由于该闭包中可能会调用阻塞协程的方法，所以需要设置成suspend
     * @param error     捕获block闭包中所有的异常，不存在协程嵌套和阻塞的方法，因此可以完全捕获
     */
    open fun startLaunch(
        block: suspend CoroutineScope.() -> Unit,
        error: ((Throwable) -> Unit)?
    ) {
        /**
         * viewModelScope作用域保证了所有的协程一定会在vm的onCleared状态时被清除
         */
        currentJob = viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                error?.invoke(e)
            }
        }
    }

    /**
     * 仅提供取消最近一次协程的功能
     */
    open fun closeCurrentCoroutine(){
        currentJob?.cancel()
    }

    /**
     *
     * viewModelScope作用域下的协程虽然会自动清理取消（在activity生命周期为onDestroy的时候，会拿到ViewModelStore，
     * 并调用其clear()方法），但是有些时候需要在非onDestroy的时候手动取消所有的协程
     */
    open fun cancelAllCoroutine() {
        viewModelScope.cancel()
    }
}