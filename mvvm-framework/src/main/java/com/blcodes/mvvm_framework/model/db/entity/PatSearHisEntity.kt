package com.blcodes.mvvm_framework.model.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * use like this.todo
 */
@Entity(tableName = "patient_search_history")
data class PatSearHisEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id")
    var id: Int? = null,

    @ColumnInfo(name = "patient_name")
    var patientName: String = ""
)