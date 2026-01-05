package com.ssavice.model

data class Review(
    val userName: String,
    val comment: String,
    val serviceName: String,
    val createdAt: Date,
    val rating: Int
)
