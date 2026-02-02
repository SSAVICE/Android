package com.ssavice.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WishServiceDTO(
    val targetStatus: Boolean
)
