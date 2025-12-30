import android.annotation.SuppressLint
import android.util.Log
import com.ssavice.model.Seller.SellerMainInfo
import com.ssavice.model.Service.ServiceSummary
import kotlinx.serialization.Serializable
import java.net.URL
import java.time.LocalDateTime

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GetCompanyInfoDTO(
    val companyId: Long,
    val companyName: String,
    val ownerName: String,
    val phoneNumber: String,
    val imageUrl: String,
    val businessNumber: String,
    val longitude: Double,
    val latitude: Double,
    val description: String,
    val detail: String,
    val service: List<ServiceDTO>,
    val review: List<ReviewDTO>,
) {
    fun toSellerMainInfoModel(): SellerMainInfo =
        SellerMainInfo(
            companyName = companyName,
            ownerName = ownerName,
            phoneNumber = phoneNumber,
            businessNumber = businessNumber,
            description = description,
            services =
                service.map {
                    val timeParsed =
                        try {
                            LocalDateTime.parse(it.deadline)
                        } catch (e: Exception) {
                            Log.e("KSC", "Invalid Time Format")
                            LocalDateTime.MIN
                        }
                    ServiceSummary(
                        name = it.title,
                        id = it.serviceId,
                        image = URL(it.serviceImageUrl),
                        latitude = it.latitude,
                        longitude = it.longitude,
                        currentMember = it.currentMember.toInt(),
                        minimumMember = it.minimumMember.toInt(),
                        basePrice = it.basePrice,
                        discountRatio = it.discountRatio,
                        discountedPrice = it.discountedPrice,
                        deadLine = timeParsed,
                        serviceTag = it.tag,
                    )
                },
        )
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ServiceDTO(
    val serviceId: Long,
    val serviceImageUrl: String?,
    val category: String,
    val companyId: Long,
    val companyName: String,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val region1: String,
    val region2: String,
    val currentMember: Long,
    val minimumMember: Long,
    val maximumMember: Long,
    val basePrice: Long,
    val discountRatio: Double,
    val discountedPrice: Long,
    val deadline: String,
    val tag: List<String>,
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
