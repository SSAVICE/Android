package com.ssavice.network.model.company

import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.network.model.RegionPostDTO
import kotlinx.serialization.Serializable

@Serializable
class AddCompanyDTO(
    val verifyToken: String,
    val companyName: String,
    val ownerName: String,
    val businessName: String,
    val startDate: String,
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
                region = RegionPostDTO.Companion.fromModel(sellerInfo.region),
                businessName = sellerInfo.businessName,
                startDate =
                    "%04d%02d%02d".format(
                        sellerInfo.companyOpenDate.year,
                        sellerInfo.companyOpenDate.month,
                        sellerInfo.companyOpenDate.day,
                    ),
            )
    }
}
