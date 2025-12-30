package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.seller.SellerRegisterForm
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
class AddCompanyDTO(
    val companyName: String,
    val ownerName: String,
    val phoneNumber: String,
    val businessNumber: String,
    val description: String,
    val accountNumber: String,
    val detail: String,
    val longitude: Double,
    val latitude: Double,
    val postCode: String,
    val address: String,
    val detailAddress: String,
    // TODO: SHOULD BE CHANGED
    val depositor: String,
) {
    fun toModel(): SellerRegisterForm =
        SellerRegisterForm(
            companyName = companyName,
            businessOwnerName = ownerName,
            phoneNumber = phoneNumber,
            businessNumber = businessNumber,
            accountNumber = accountNumber,
            description = description,
            detail = detail,
            region =
                com.ssavice.model.RegionInfo(
                    longitude = longitude,
                    latitude = latitude,
                    postCode = postCode,
                    address = address,
                    detailAddress = detailAddress,
                ),
            accountDepositor = depositor,
        )

    companion object {
        fun fromModel(sellerInfo: SellerRegisterForm): AddCompanyDTO =
            AddCompanyDTO(
                companyName = sellerInfo.companyName,
                ownerName = sellerInfo.businessOwnerName,
                phoneNumber = sellerInfo.phoneNumber,
                businessNumber = sellerInfo.businessNumber,
                accountNumber = sellerInfo.accountNumber,
                description = sellerInfo.description,
                detail = sellerInfo.detail,
                longitude = sellerInfo.region.longitude,
                latitude = sellerInfo.region.latitude,
                postCode = sellerInfo.region.postCode,
                address = sellerInfo.region.address,
                detailAddress = sellerInfo.region.detailAddress,
                depositor = sellerInfo.accountDepositor,
            )
    }
}
