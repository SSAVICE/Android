package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.SellerInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
class AddCompanyDTO(
    val companyName: String,
    val ownerName: String,
    val phoneNumber: String,
    val businessNumber: String,
    val accountNumber: String,
    val description: String,
    val detail: String,
    val longitude: Double,
    val latitude: Double,
    val postCode: String,
    val address: String,
    val detailAddress: String,
    // TODO: SHOULD BE CHANGED
    val depositor: String,
) {
    fun toModel(): SellerInfo =
        SellerInfo(
            companyName = companyName,
            ownerName = ownerName,
            phoneNumber = phoneNumber,
            businessNumber = businessNumber,
            accountNumber = accountNumber,
            description = description,
            detail = detail,
            longitude = longitude,
            latitude = latitude,
            postCode = postCode,
            address = address,
            detailAddress = detailAddress
        )

    companion object {
        fun fromModel(sellerInfo: SellerInfo): AddCompanyDTO =
            AddCompanyDTO(
                companyName = sellerInfo.companyName,
                ownerName = sellerInfo.ownerName,
                phoneNumber = sellerInfo.phoneNumber,
                businessNumber = sellerInfo.businessNumber,
                accountNumber = sellerInfo.accountNumber,
                description = sellerInfo.description,
                detail = sellerInfo.detail,
                longitude = sellerInfo.longitude,
                latitude = sellerInfo.latitude,
                postCode = sellerInfo.postCode,
                address = sellerInfo.address,
                detailAddress = sellerInfo.detailAddress,
                depositor = "Test",
            )
    }
}
