package com.blcodes.mvvm_framework.utils

/**
 * 集合中，如果某一个元素包含predicate的条件，则属于contains关系
 */
public inline fun <T> Iterable<T>.innerContains(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}