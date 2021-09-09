package com.blcodes.mvvm_framework.model.db.converter

import androidx.room.TypeConverter
import com.blcodes.mvvm_framework.model.data.LoginBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * use like this.todo
 */
class UserConverter {
    @TypeConverter
    fun stringToObject(value: String): MutableList<LoginBean.RoleList> {
        val listType = object : TypeToken<MutableList<LoginBean.RoleList>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: MutableList<LoginBean.RoleList>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}