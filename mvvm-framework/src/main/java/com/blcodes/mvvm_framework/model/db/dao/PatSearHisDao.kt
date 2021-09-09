package com.blcodes.mvvm_framework.model.db.dao

import androidx.room.*
import com.blcodes.mvvm_framework.model.db.entity.PatSearHisEntity

/**
 * use like this. todo
 */
@Dao
interface PatSearHisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PatSearHisEntity)

    @Delete
    suspend fun delete(entity: PatSearHisEntity)

    @Update
    suspend fun update(entity: PatSearHisEntity)

    @Query("select * from patient_search_history where history_id = :id")
    suspend fun searchById(id: Int): PatSearHisEntity?

    @Query("SELECT * FROM patient_search_history WHERE patient_name LIKE '%' || :name || '%' ")
    suspend fun searchByName(name: String): List<PatSearHisEntity>?

    @Query("SELECT * FROM patient_search_history WHERE patient_name =:name ")
    suspend fun searchByNameOnly(name: String): PatSearHisEntity?

    @Query("delete from patient_search_history where history_id = :id")
    suspend fun deleteById(id: Int)

    @Query("select * from patient_search_history")
    suspend fun queryAll(): List<PatSearHisEntity>?

    @Query("delete from patient_search_history")
    suspend fun deleteAll()
}