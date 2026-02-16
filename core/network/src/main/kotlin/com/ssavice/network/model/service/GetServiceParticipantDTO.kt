package com.ssavice.network.model.service

import com.ssavice.model.service.ServiceParticipant
import com.ssavice.model.service.ServiceParticipantResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetServiceParticipantDTO(
    val content: List<Participant>,
    val currentPage: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
) {
    fun toModel(): ServiceParticipantResponse =
        ServiceParticipantResponse(
            items = content.map { it.toModel() },
            currentPage = currentPage,
            size = size,
            totalElements = totalElements,
        )
}

@Serializable
data class Participant(
    val bookId: Long,
    val name: String,
    val thumbnailUrl: String,
    val userId: Long,
) {
    fun toModel(): ServiceParticipant =
        ServiceParticipant(
            name = name,
            thumbnailUrl = thumbnailUrl,
            userId = userId,
        )
}
