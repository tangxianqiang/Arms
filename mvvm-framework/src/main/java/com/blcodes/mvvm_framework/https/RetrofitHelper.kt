package com.blcodes.mvvm_framework.https

import com.blcodes.mvvm_framework.HIS_URL
import com.blcodes.mvvm_framework.utils.ResponseFormatUtil
import okhttp3.OkHttpClient
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * object可以用来匿名对象声明，这里用来实现一个饿汉单例，获取retrofit实例不需要线程安全
 */
object RetrofitHelper {
    fun getHttpsClient():Retrofit{
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor {
                //可以修改header
                val originalRequest = it.request().apply {
                    val sbStr = StringBuilder("网络请求（${method()}）：${url()}")
                    val requestBody = body()
                    if (requestBody != null) {
                        val buffer = Buffer()
                        requestBody.writeTo(buffer)
                        sbStr.append("$\n${buffer.readString(Charset.forName("UTF-8"))}")
                    }
                    ResponseFormatUtil.printJson(sbStr.toString())
                }
                //如添加token
                val token = ""
                val response = if (token.isNullOrEmpty()) {
                    it.proceed(originalRequest)
                } else {
                    val tokenRequest =
                        originalRequest.newBuilder().header("Authorization", token)
                            .build()

                    it.proceed(tokenRequest)
                }

                // 将结果进行格式化之后打印
                val responseBody = response.peekBody(Long.MAX_VALUE)
                val jsonReader = InputStreamReader(responseBody.byteStream(), "UTF-8")
                val reader = BufferedReader(jsonReader)
                val sbJson = StringBuilder()
                var line = reader.readLine()
                do {
                    sbJson.append(line)
                    line = reader.readLine()
                } while (line != null)

                ResponseFormatUtil.printJson(sbJson.toString())

                response
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(HIS_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}