package com.ssavice.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ConfirmImageDTO(
    val objectKey: String,
)
