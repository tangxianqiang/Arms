package com.blcodes.mvvm_framework

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main(args: Array<String>) {
    //场景1、简化callback回调
    // 获取网络无非拿一个值，不要考虑线程切换、不要考虑线程并发和调度、不要考虑怎么写回调，
    // 拿到了就监听值的变化处理UI，没有拿到就不管，毕竟View更多是更具数据来驱动的。
    // 那么相当于就是一个被View监听的data变量，并且直接请求响应的接口，给这个data变量赋值就完了
    getRemoteData()
}

fun getRemoteData() = runBlocking {
//    GlobalScope.launch {
//        println("1、当前线程是 ${Thread.currentThread().name}")
//
//        val firstData = getFirst()
//        val secondData = getSecond(firstData)
//
//        println("7、结果是：$secondData，当前线程是 ${Thread.currentThread().name}")
//    }
//    println("6、我最先执行，当前线程是 ${Thread.currentThread().name}")
//    GlobalScope.launch {
//        val third = getThird()
//        println("5、结果是：$third，当前线程是 ${Thread.currentThread().name}")
//    }
//    var a = 0
//    launch{
//        println("1、我最先执行，当前线程是 ${Thread.currentThread().name}")
//        a = withContext(Dispatchers.IO){
//            println("2、当前线程是 ${Thread.currentThread().name}")
//            Thread.sleep(4000)
//            println("3、当前线程是 ${Thread.currentThread().name}")
//            10
//        }
//        println("--------- result : $a       thread : ${Thread.currentThread().name}")
//    }
    var a = 0
    launch {
        println("1、我最先执行，当前线程是 ${Thread.currentThread().name}")
        a = suspendCoroutine {
            println("2、当前线程是 ${Thread.currentThread().name}")
            Thread {
                Thread.sleep(4000)
                it.resume(10)
                println("3、当前线程是 ${Thread.currentThread().name}")
            }.start()
            println("4、当前线程是 ${Thread.currentThread().name}")
        }
        println("--------- result : $a       thread : ${Thread.currentThread().name}")
    }

    delay(100000)
}

suspend fun getThird(): Int = withContext(Dispatchers.IO){
    delay(1000)
    println("4、获取第三个数据，当前线程是 ${Thread.currentThread().name}")
    3
}


suspend fun getSecond(firstData: Int): Int = withContext(Dispatchers.IO){
    getSecondIO()
    2 + firstData
}
/**
 * 用withContext将代码块中的耗时操作放在IO线程中，获取线程后自动切回线程
 */
suspend fun getFirst(): Int = withContext(Dispatchers.IO){
    getFirstIO()
    1
}

suspend fun getFirstIO() {
    Thread.sleep(4000)
    println("2、获取第一个数据,当前线程是 ${Thread.currentThread().name}")
}

suspend fun getSecondIO() {
    Thread.sleep(1000)
    println("3、获取第二个数据，当前线程是 ${Thread.currentThread().name}")
}
