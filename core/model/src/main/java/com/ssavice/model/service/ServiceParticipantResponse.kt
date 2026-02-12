package com.ssavice.model.service

data class ServiceParticipantResponse(
    val items: List<ServiceParticipant>,
    val currentPage: Int,
    val size: Int,
    val totalElements: Long,
)

data class ServiceParticipant(
    val name: String,
    val thumbnailUrl: String,
    val userId: Long,
)
