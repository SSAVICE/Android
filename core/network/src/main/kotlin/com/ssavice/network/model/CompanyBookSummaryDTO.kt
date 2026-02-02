package com.ssavice.network.model

import com.ssavice.model.user.ParticipationSummary
import kotlinx.serialization.Serializable

@Serializable
data class CompanyBookSummaryDTO(
    val applying: Long,
    val completed: Long,
    val total: Long,
) {
    fun toModel(): ParticipationSummary =
        ParticipationSummary(
            onProgress = applying.toInt(),
            done = completed.toInt(),
            total = total.toInt(),
        )
}
