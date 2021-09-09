package com.blcodes.mvvm_framework.model.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blcodes.mvvm_framework.model.db.dao.PatSearHisDao
import com.blcodes.mvvm_framework.model.db.dao.UserDao
import com.blcodes.mvvm_framework.model.db.entity.PatSearHisEntity
import com.blcodes.mvvm_framework.model.db.entity.TokenEntity
import com.blcodes.mvvm_framework.model.db.entity.UserEntity

/**
 * use like this.todo
 */
@Database(version = 1, entities = [UserEntity::class, TokenEntity::class, PatSearHisEntity::class], exportSchema = false)
abstract class AppDb : RoomDatabase() {
    companion object {
        private lateinit var instance: AppDb

        @Synchronized
        fun getDataBase(application: Application): AppDb {
            return if (Companion::instance.isInitialized) {
                instance
            } else {
                Room.databaseBuilder(application, AppDb::class.java, "app_database")
                    .allowMainThreadQueries()
                    .build()
                    .apply {
                        instance = this
                    }
            }
        }
    }

    abstract fun userDao(): UserDao

    abstract fun patSearHisDao(): PatSearHisDao
}