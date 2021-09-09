package com.blcodes.mvvm_framework.model.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * use like this.todo
 */
@Entity(tableName = "token")
data class TokenEntity(
    /*主键index*/
    @PrimaryKey(autoGenerate = true)
    var keyId: Long = 0,

    @ColumnInfo val access_token: String?,
    @ColumnInfo val token_type: String?,
    @ColumnInfo val refresh_token: String?,
    @ColumnInfo val expires_in: Int?,
    @ColumnInfo val scope: String?,
    @ColumnInfo val Provider: String?,
    @ColumnInfo val jti: String?
)