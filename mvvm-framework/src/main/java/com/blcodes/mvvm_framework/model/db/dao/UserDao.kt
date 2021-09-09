package com.blcodes.mvvm_framework.model.db.dao

import androidx.room.*
import com.blcodes.mvvm_framework.model.db.entity.UserEntity

/**
 * use like this.todo
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Query("select * from user where user_id = :id")
    fun searchById(id: String): UserEntity?

    @Query("delete from user where user_id = :id")
    suspend fun deleteById(id: String)

    @Query("select * from user")
    suspend fun queryAll(): List<UserEntity>?

    /**
     * 立即删除,在UI线程操作
     */
    @Delete
    fun deleteLimit(userEntity: UserEntity)

    @Query("delete from user where user_id = :id")
    fun deleteLimitById(id: String)
}