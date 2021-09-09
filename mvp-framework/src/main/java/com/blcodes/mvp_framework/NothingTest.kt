package com.blcodes.mvp_framework

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

var remoteData: String? = null

fun main(args: Array<String>) {
    var data = ""
    getRemoteData()
    runBlocking {
        delay(3000)
        data = remoteData ?: fail("null err")
        println("after data:$data")
    }
}

fun fail(str: String): Nothing {
    throw IllegalAccessException(str)
}

fun getRemoteData() {
    GlobalScope.launch {
//        remoteData = "The data"
        delay(1000)
    }
}