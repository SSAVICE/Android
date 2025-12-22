package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.SellerInfo
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
class AddCompanyDTO(
    val companyName: String,
    val ownerName: String,
    val phoneNumber: String,
    val imageUrl: String,
    val businessNumber: String,
    val accountNumber: String,
    val longitude: Double,
    val latitude: Double,
    val description: String,
    val detail: String,
) {
    fun toModel(): SellerInfo =
        SellerInfo(
            companyName = companyName,
            ownerName = ownerName,
            phoneNumber = phoneNumber,
            imageUrl = imageUrl,
            businessNumber = businessNumber,
            accountNumber = accountNumber,
            longitude = longitude,
            latitude = latitude,
            description = description,
            detail = detail
        )

    companion object {
        fun fromModel(sellerInfo: SellerInfo): AddCompanyDTO = AddCompanyDTO(
            companyName = sellerInfo.companyName,
            ownerName = sellerInfo.ownerName,
            phoneNumber = sellerInfo.phoneNumber,
            imageUrl = sellerInfo.imageUrl,
            businessNumber = sellerInfo.businessNumber,
            accountNumber = sellerInfo.accountNumber,
            longitude = sellerInfo.longitude,
            latitude = sellerInfo.latitude,
            description = sellerInfo.description,
            detail = sellerInfo.detail
        )
    }
}
