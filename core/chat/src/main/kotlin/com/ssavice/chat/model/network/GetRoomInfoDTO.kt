package com.ssavice.chat.model.network

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomInfoDTO(
    val roomId: String,
    val name: String,
    val roomType: String,
    val serviceId: Long,
    val participants: List<RoomParticipantDTO>,
)

@Serializable
data class RoomParticipantDTO(
    val name: String,
    val userId: Long,
    val thumbnail: String,
)
