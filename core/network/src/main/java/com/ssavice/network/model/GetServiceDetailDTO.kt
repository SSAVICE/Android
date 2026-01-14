package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.Date
import com.ssavice.model.RegionInfo
import com.ssavice.model.service.ServiceDetail
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GetServiceDetailDTO(
    val imageUrl: List<String>,
    val serviceId: Long,
    val category: String,
    val companyId: Long,
    val title: String,
    val description: String,
    val currentMember: Long,
    val minimumMember: Long,
    val maximumMember: Long,
    val basePrice: Long,
    val discountRatio: Double,
    val discountedPrice: Long,
    val deadline: String,
    val tag: String,
    val startDate: String,
    val endDate: String,
    val isLiked: Boolean,
    val status: String,
    val createdAt: String,
    val region: RegionDTO,
) {
    fun toModel(): ServiceDetail =
        ServiceDetail(
            imageUrls = imageUrl,
            id = serviceId,
            category = category,
            companyId = companyId,
            name = title,
            description = description,
            tag = tag,
            basePrice = basePrice.toInt(),
            discountRatio = discountRatio.toInt(),
            discountedPrice = discountedPrice.toInt(),
            minimumMember = minimumMember.toInt(),
            maximumMember = maximumMember.toInt(),
            currentMember = currentMember.toInt(),
            deadLine = Date.parse(deadline),
            startDate = Date.parse(startDate),
            endDate = Date.parse(endDate),
            liked = isLiked,
            status = status,
            createdAt = Date.parse(createdAt),
            regionInfo =
                region.toModel(),
        )
}
