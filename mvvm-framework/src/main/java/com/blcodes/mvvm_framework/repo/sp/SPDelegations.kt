package com.blcodes.mvvm_framework.repo.sp

import android.os.Parcelable
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SetDelegation(
    private val instance: MMKV, private val default: Set<String>? = null
) : ReadWriteProperty<Any, Set<String>?> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, value)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Set<String>? {
        return instance.decodeStringSet(property.name, default)
    }
}

class StringDelegation(
    private val instance: MMKV, private val default: String? = null
) : ReadWriteProperty<Any, String?> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, value)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return instance.decodeString(property.name, default)
    }
}

class BoolDelegation(
    private val instance: MMKV, private val default: Boolean = false
) : ReadWriteProperty<Any, Boolean> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        instance.encode(property.name, value)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return instance.decodeBool(property.name, default)
    }
}

class IntDelegation(
    private val instance: MMKV, private val default: Int = 0
) : ReadWriteProperty<Any, Int?> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, value)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return instance.decodeInt(property.name, default)
    }
}

class LongDelegation(
    private val instance: MMKV, private val default: Long = 0L
) : ReadWriteProperty<Any, Long?> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, value)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return instance.decodeLong(property.name, default)
    }
}

class FloatDelegation(
    private val instance: MMKV, private val default: Float = 0F
) : ReadWriteProperty<Any, Float?> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, value)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return instance.decodeFloat(property.name, default)
    }
}

class BytesDelegation(
    private val instance: MMKV, private val default: ByteArray? = null
) : ReadWriteProperty<Any, ByteArray?> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: ByteArray?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, value)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): ByteArray? {
        return instance.decodeBytes(property.name, default)
    }
}

class ParcelableDelegation<T : Parcelable>(
    private val instance: MMKV,
    private val tClass: Class<T>,
    private val default: T? = null
) : ReadWriteProperty<Any, T?> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, value)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return instance.decodeParcelable(property.name, tClass, default)
    }
}

class ObjectDelegation<T>(
    private val instance: MMKV, private val clazz: Class<T>, private val default: T? = null
) : ReadWriteProperty<Any, T?> {
    private val gson = Gson()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        if (value == null) {
            instance.removeValueForKey(property.name)
        } else {
            instance.encode(property.name, gson.toJson(value))
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val jsonStr = instance.decodeString(property.name)

        return if (jsonStr != null) {
            gson.fromJson(jsonStr, clazz)
        } else {
            default
        }
    }
}