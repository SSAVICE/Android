package com.ssavice.network.model.service

import kotlinx.serialization.Serializable

@Serializable
data class WishServiceDTO(
    val targetStatus: Boolean,
)
