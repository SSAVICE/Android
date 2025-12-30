import android.annotation.SuppressLint
import com.ssavice.model.Date
import com.ssavice.model.RegionInfo
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.service.ServiceSummary
import kotlinx.serialization.Serializable
import java.net.URL

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GetCompanyInfoDTO(
    val companyId: Long,
    val companyName: String,
    val phoneNumber: String,
    val imageUrl: String?,
    val businessNumber: String,
    val longitude: Double,
    val latitude: Double,
    val postCode: String,
    val address: String,
    val detailAddress: String,
    val description: String,
    val detail: String,
    val service: List<ServiceDTO>,
) {
    fun toSellerMainInfoModel(): SellerMainInfo =
        SellerMainInfo(
            companyName = companyName,
            phoneNumber = phoneNumber,
            businessNumber = businessNumber,
            description = description,
            services =
                service.map {
                    ServiceSummary(
                        name = it.title,
                        id = it.serviceId,
                        image = URL(it.serviceImageUrl),
                        currentMember = it.currentMember.toInt(),
                        minimumMember = it.minimumMember.toInt(),
                        basePrice = it.basePrice,
                        discountRatio = it.discountRate,
                        discountedPrice = it.discountedPrice,
                        deadLine = Date.parse(it.deadline),
                        startDate = Date.parse(it.startDate),
                        endDate = Date.parse(it.endDate),
                        serviceTag = it.tag,
                        category = it.category,
                    )
                },
            region = RegionInfo(
                latitude = latitude,
                longitude = longitude,
                address = address,
                detailAddress = detailAddress,
                postCode = postCode
            )
        )
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ServiceDTO(
    val serviceId: Long,
    val serviceImageUrl: String?,
    val category: String,
    val title: String,
    val currentMember: Long,
    val minimumMember: Long,
    val maximumMember: Long,
    val description: String,
    val basePrice: Long,
    val discountRate: Double,
    val discountedPrice: Long,
    val status: String,
    val startDate: String,
    val endDate: String,
    val deadline: String,
    val tag: String,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReviewDTO(
    val userName: String,
    val comment: String,
    val serviceName: String,
    val createdAt: String,
    val rate: Int,
)
