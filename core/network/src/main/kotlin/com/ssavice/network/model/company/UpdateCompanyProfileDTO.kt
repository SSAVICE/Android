package com.ssavice.network.model.company

import com.ssavice.model.seller.SellerProfileUpdateForm
import com.ssavice.network.model.RegionPostDTO
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCompanyProfileDTO(
    val companyName: String,
    val description: String,
    val detail: String,
    val phoneNumber: String,
    val region: RegionPostDTO,
) {
    companion object {
        fun fromModel(model: SellerProfileUpdateForm): UpdateCompanyProfileDTO =
            UpdateCompanyProfileDTO(
                companyName = model.sellerName,
                description = model.description,
                detail = model.detail,
                phoneNumber = model.phoneNumber,
                region = RegionPostDTO.Companion.fromModel(model.region),
            )
    }
}
