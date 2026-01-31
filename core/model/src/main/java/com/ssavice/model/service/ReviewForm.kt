package com.ssavice.model.service

data class ReviewForm(
    val serviceId: Long,
    val sellerId: Long,
    val rating: Int,
    val content: String,
)
