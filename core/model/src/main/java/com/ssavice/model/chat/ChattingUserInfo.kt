package com.ssavice.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChattingUserInfo(
    val name: String,
    val id: Long,
    val thumbnail: String,
    val lastUpdate: Long = System.currentTimeMillis()
) {
    fun needRefresh(): Boolean {
        return (System.currentTimeMillis() - lastUpdate) > USER_INFO_REFRESH_CYCLE
    }

    companion object {
        const val USER_INFO_REFRESH_CYCLE = 1000 * 3600 * 2
    }
}
