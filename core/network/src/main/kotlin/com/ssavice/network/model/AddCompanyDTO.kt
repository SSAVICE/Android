package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.seller.SellerRegisterForm
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
class AddCompanyDTO(
    val verifyToken: String,
    val companyName: String,
    val ownerName: String,
    val phoneNumber: String,
    val businessNumber: String,
    val description: String,
    val depositor: String,
    val accountNumber: String,
    val detail: String,
    val region: RegionPostDTO,
) {
    companion object {
        fun fromModel(
            sellerInfo: SellerRegisterForm,
            verifyToken: String,
        ): AddCompanyDTO =
            AddCompanyDTO(
                verifyToken = verifyToken,
                companyName = sellerInfo.companyName,
                ownerName = sellerInfo.businessOwnerName,
                phoneNumber = sellerInfo.phoneNumber,
                businessNumber = sellerInfo.businessNumber,
                accountNumber = sellerInfo.accountNumber,
                description = sellerInfo.description,
                detail = sellerInfo.detail,
                depositor = sellerInfo.accountDepositor,
                region = RegionPostDTO.fromModel(sellerInfo.region),
            )
    }
}
