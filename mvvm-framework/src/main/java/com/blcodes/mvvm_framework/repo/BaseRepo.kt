package com.blcodes.mvvm_framework.repo

import android.os.Handler
import android.os.Looper
import com.blcodes.mvvm_framework.BaseException
import com.blcodes.mvvm_framework.https.RetrofitHelper
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 提供CPS的数据获取能力，如果是操作文件、数据库等，建议直接使用withContext(Dispatchers.IO),
 * 如果使用retrofit，先准备client和接口
 */
class BaseRepo {

    /**
     * 获取到retrofit的实例和api接口实例
     */
    open inline fun <reified T> getApiService(): T {
        return RetrofitHelper.getHttpsClient().create(T::class.java)
    }

    /**
     * 在网络请求中，retrofit可以通过动态，返回一个call对象，通过call对象同步或者异步拿到回调，最后在call
     * 对象的回调中，可以将值给主线程，不过需要先切换到主线程，并且这是一个异步过程
     *
     * CPS：
     * 考虑到真实逻辑处理中，存在一个请求依赖上个请求的结果的情况：
     * A{ a ->
     *      B(a){ b ->
     *          C(b){ c ->
     *              doSomething(c)
     *          }
     *      }
     * }
     * 类似这样的回调虽然有理可循，但是回调太多影响代码的结构，而且过于复杂
     * 如果可以以流式的代码来写这里的逻辑，结构更加清晰
     * a = A();
     * b = B(a);
     * c = C(b);
     * doSomething(c);
     * call不满足这样的执行，因为在协程中使用IO调度异步执行，后面的逻辑等不到上次异步执行的结果就开始执行了，需要手动先将
     * 协程阻塞挂起，等到异步结果拿到后才resume协程，继续执行协程后面的逻辑，即：
     *      开启协程 ->
     *          切换到IO线程执行A()耗时操作 ->
     *              挂起协程等待耗时操作执行完成 ->
     *                  A()耗时操作完成，resume协程，将回调结果a返回 ->
     *                      执行B(a)......
     * withContext(Dispatchers.IO)可以切换线程，但是它无法直接阻塞协程，只要withContext中的block执行完成，
     * 就会马上切换到withContext调用之前的线程。[suspendCoroutine]可以阻塞协程，但是不负责线程切换。
     *
     * 因此以上CPS可以通过withContext(Dispatchers.IO) + 阻塞代码块完成，也可以通过异步的suspendCoroutine完成
     *
     * BaseRepo中的协程是可以取消的：
     * withContext中调用[suspendCancellableCoroutine]挂起了协程，那么其中的任何操作都是可以取消的，就像delay一样，取消这个协程是是可以
     * 抛出[CancellationException]异常的，不需要再判断isActive。相反，如果在协程中的大型代码运行在另外一个线程，处于一个计算过程，并且
     * 没有检查取消状态，也就是不属于挂起代码，那么非挂起代码是不会检查取消状态，那么久不会被取消！
     *
     * await是一个耗时的代码，需要在IO线程中执行，可以使用withContext自动切换
     */
    suspend fun <T> Call<T>.await(): T {
        //协程取消时，suspendCancellableCoroutine会抛出CancellationException异常
        //invokeOnCancellation通过异常执行到这里，可以做一些后续操作
        return suspendCancellableCoroutine {
            it.invokeOnCancellation {
                //清理一些数据
            }
            //使用call的同步方法，保持网络请求在withContext的IO线程中
            //如果使用异步方法也是可以的，不过会多余的开启其他线程，造成浪费，并且异步的回调会被withContext切换成之前的线程，
            //因为withContext中的代码在异步回调之前已经早就执行完了
            val data = execute()
            if (data.isSuccessful) {
                //如果有逻辑错误也可以抛出异常，具体的异常处理自行处理
                //it.resume(data.body()!!)
            } else {
                it.resumeWithException(BaseException(data.code().toString()))
            }
        }
    }

    /**
     * awaitAsync同样支持CPS，并且正是由于suspendCancellableCoroutine挂起了协程，让其中的异步任务先完成
     */
    suspend fun <T> Call<T>.awaitAsync(): T {
        return suspendCancellableCoroutine {
            it.invokeOnCancellation {
                //清理一些数据
            }
            //enqueue使用的是okhttp开启的线程，并且如果涉及到线程池，也是okhttp的线程池策略
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    //此处处于IO线程，徐娅切换回来
                    Handler(Looper.getMainLooper()!!).post {
//                        it.resume(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(BaseException(t.message ?: "error"))
                }

            })
        }
    }

}