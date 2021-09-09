package com.blcodes.mvvm_framework.model.db.entity

import androidx.room.*
import com.blcodes.mvvm_framework.model.db.converter.UserConverter

/**
 * use like this. todo
 */
@Entity(tableName = "user")
@TypeConverters(UserConverter::class)
data class UserEntity(
    var index: Long? = null,

    /*用户名,用户登录时的account*/
    @ColumnInfo(name = "user_account")
    var userAccount: String? = null,

    /*用户的token信息,直接设置在header中的*/
    @ColumnInfo(name = "user_token")
    var userToken: String? = null,

    /*密码密码存在本地用于记住密码功能的使用，暂时未加密 */
    @ColumnInfo(name = "user_pwd")
    var userPwd: String? = null,

    /*用户的公钥*/
    @ColumnInfo(name = "user_public_key")
    var publicKey: String? = null,



    @PrimaryKey()
    @ColumnInfo(name = "user_id")
    var id: String = "",

    @ColumnInfo(name = "user_nick_name")
    var nickName: String? = null,

    @ColumnInfo(name = "user_avatar")
    var avatar: String? = null,

    @ColumnInfo(name = "user_permission")
    var permission: String? = null,

    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded()
    var token: TokenEntity?

){
    //使用默认构造函数
}