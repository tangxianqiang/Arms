package com.blcodes.mvvm_framework.model.data

import java.io.Serializable

/**
 * demo. todo
 */
data class LoginBean(
    val id: String?,
    val loginTime: String?,
    val roleList: MutableList<RoleList>?,
    val nickName: String?,
    val token: Token?,
    val avatar: String?
) {
    data class RoleList(
        val id: String?,
        val name: String?,
        val appDomainId: String?,
        val defaultRole: Int?,
        val appDomainName: String?,
        val remark: String?,
        val createUserId: String?,
        val createTime: String?,
        val delFlag: Int?
    )

    data class Permission(
        val bqdm: String?,
        val bqmc: String?
    ): Serializable

    data class Token(
        val access_token: String?,
        val token_type: String?,
        val refresh_token: String?,
        val expires_in: Int?,
        val scope: String?,
        val Provider: String?,
        val jti: String?
    )
}