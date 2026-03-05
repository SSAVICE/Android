package com.ssavice.network.model.user

import com.ssavice.model.chat.ChattingUserInfo
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoDTO(
    val list: List<UserInfoDTO>,
) {
    fun toModel(): List<ChattingUserInfo> =
        list.map {
            ChattingUserInfo(
                name = it.name,
                id = it.id,
                thumbnail = it.thumbnail,
            )
        }
}

@Serializable
data class UserInfoDTO(
    val name: String,
    val id: Long,
    val thumbnail: String,
)
