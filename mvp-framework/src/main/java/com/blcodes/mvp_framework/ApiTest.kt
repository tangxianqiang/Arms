package com.blcodes.mvp_framework

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


fun main(args: Array<String>) {
    //launch 是协程构建器
    //CoroutineScope 是上下文环境
    //GlobalScope.launch{} 可以启动一个协程，表示这个协程的上下文环境是GlobalScope，并且生命周期是全局的
    //delay 是一个特殊的挂起函数，它不会造成线程阻塞，但是会挂起协程
    //delay函数是一个suspend函数，这种suspend函数不会阻塞线程，但是会阻塞协程，那delay直接脱离协程使用毫无意义，
    //所以直接使用suspend函数会报错，并且它们只有在协程中使用才有意义

    //runBlocking{delay(2000)} 可以等价于在主线程中直接调用sleep(2000)，这个协程实际上是和主线程绑定的，用于单元测试
    //两个线程抢占cpu资源，如何保证其他线程的作业先执行？
    //launch协程构造器可以返回一个Job类型的对象：
//    val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
//        delay(1000L)
//        println("World!")
//    }
//    println("Hello,")
//    runBlocking {
//        job.join() // 等待直到子协程执行结束
//    }
    //join()解决了阻塞时间的不足

    //GlobalScope：老是使用顶层的协程作用域（上下文环境）可能会导致内存泄漏或者占用过多资源，使用顶层协程实际上就和直接使用线程一样
    //作用域构造器：外部协程直到在其作用域中启动的所有协程都执行完毕后才会结束
    //因此：
//    CoroutineScope(Dispatchers.IO).launch { // this: CoroutineScope
//        this.launch { // 在 runBlocking 作用域中启动一个新协程
//            delay(1000L)
//            println("World!"+Thread.currentThread())
//        }
//        delay(1000L)
//        println("Hello,"+Thread.currentThread())
//    }
//    runBlocking {     // 但是这个表达式阻塞了主线程
//        delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
//    }
//    println("end!"+Thread.currentThread())
//    Thread.sleep(2000)

//    Thread{
//        println("other thread!"+Thread.currentThread().id+ Thread.currentThread().name + Thread.currentThread().threadGroup)
//        Thread.sleep(2000)
//        println("other thread dead!"+Thread.currentThread())
//    }.start()

    //中，runBlocking会阻塞，直到内部协程执行完毕，等同于下面的runBlocking中的coroutineScope
//     runBlocking {
//         coroutineScope { // this: CoroutineScope
//             launch { // 在 runBlocking 作用域中启动一个新协程
//                 delay(1000L)
//                 println("World!")
//             }
//             println("Hello,")
//         }
//     }
    //所以runBlocking==coroutineScope？不：runBlocking 方法会阻塞当前线程来等待， 而 coroutineScope 只是挂起，
    //会释放底层线程用于其他用途， coroutineScope是挂起函数
//    public suspend fun <R> coroutineScope(block: suspend CoroutineScope.() -> R): R {


    //协程的取消
    // cancelAndJoin 它合并了对 cancel 以及 join 的调用
    //不是所有的协程的作业都可以被取消，只有协程中包含协作任务，如挂起函数，才能被取消，否则只能通过isActive来当终端判断，
    //就像线程中的while一样
    //join的作用：保证当前协程的任务完成
//    runBlocking{
//        val job = launch{
//            try {
//                repeat(1000) { i ->
//                    println("job: I'm sleeping $i ...")
//                    delay(500L)
//                }
//            } finally {
//                println("job: I'm running finally")
//            }
//        }
//        delay(1300L) // 延迟一段时间
//        println("main: I'm tired of waiting!")
//        job.cancelAndJoin() // 取消该作业并且等待它结束
//        println("main: Now I can quit.")
//    }
    //join 和 cancelAndJoin 等待了所有的终结动作执行完毕
//    GlobalScope.launch {
//        a= getA()
//        println("a  =  ${a}")
//        b = getB()
//        println("---a+ b  =  ${a + b}")
//    }
//    println("start")
//
//    CoroutineScope(Dispatchers.IO).launch {
//        val time1 = System.currentTimeMillis()
//
//        val task1 = async {
//            println("t1 start")
//            delay(4000)
//            println( "1.执行task1.... [当前]")
//            "one"  //返回结果赋值给task1
//        }
//
//        val task2 = async(Dispatchers.IO) {
//            println("t2 start")
//            delay(2000)
//            println("2.执行task2.... [当")
//            "two"  //返回结果赋值给task2
//        }
//        println("任务开始挂起")
//        println("task1 = ${task2.await()}  , task2 = ${task1.await()} , 耗时 ${System.currentTimeMillis() - time1} ms  ")//挂起了当前协程
//        println("任务结束")
//    }
//
//    runBlocking {
//        delay(10000)
//    }

//    val test = "123456||789"
//    var b = test.toByteArray()
//    println("${b.size}")
//
//    b.forEach {
//        print("${it}")
//    }






}

var a = 0
var b = 0

suspend fun getA():Int {
    return withContext(Dispatchers.IO){
        delay(3000)
        10
    }

}


suspend fun  getB():Int {
    return withContext(Dispatchers.IO){
        delay(1000)
        15
    }
}
