package com.ssavice.network.model.user

import com.ssavice.model.chat.ChattingUserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoDTO(
    @SerialName("members")
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
    @SerialName("accountId")
    val id: Long,
    @SerialName("imageUrl")
    val thumbnail: String,
)
