package com.ssavice.post_service

import android.net.Uri
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.TimeStamp
import com.ssavice.model.enums.Category
import java.net.URI

data class AddServiceUiState(
    val form: Form,
    val submitState: SubmitState,
    val imageState: ImageState,
)

data class ImageState(
    val pictureList: List<UploadingImage>,
) {
    fun add(image: UploadingImage): ImageState =
        this.let {
            val i =
                it.pictureList.indexOfLast { item ->
                    item.uri == image.uri
                }

            if (i == -1) {
                ImageState(it.pictureList + image)
            } else {
                val l = pictureList.toMutableList()
                l[i] = image
                ImageState(l)
            }
        }

    fun removeAt(index: Int): ImageState =
        this.let {
            ImageState(it.pictureList - it.pictureList[index])
        }
}

data class UploadingImage(
    val uri: Uri,
    val progress: ImageUploadProgress,
)

data class AddressForm(
    val address: String,
    val regionCode: String,
    val latitude: Double,
    val longitude: Double,
    val zipCode: String,
)

data class Form(
    val name: String = "",
    val category: Category = Category.entries[1],
    val tag: String = "",
    val minRecruit: Int,
    val maxRecruit: Int,
    val price: Int = 0,
    val addressForm: AddressForm,
    val detailAddress: String = "",
    val discountRatio: Int = 0,
    val discountedPrice: Int = 0,
    val deadline: TimeStamp,
    val startDate: TimeStamp,
    val endDate: TimeStamp,
    val description: String = "",
    val categoryList: List<Category> = Category.entries.filter { it.shownInSeller },
    val nameErrorMessage: String? = null,
    val categoryErrorMessage: String? = null,
    val tagErrorMessage: String? = null,
    val minRecruitErrorMessage: String? = null,
    val maxRecruitErrorMessage: String? = null,
    val priceErrorMessage: String? = null,
    val discountRatioErrorMessage: String? = null,
    val deadlineErrorMessage: String? = null,
    val startDateErrorMessage: String? = null,
    val endDateErrorMessage: String? = null,
    val descriptionErrorMessage: String? = null,
)

sealed interface ServiceImage {
    object Loading : ServiceImage

    data class Preprocessing(
        val uri: URI,
    ) : ServiceImage
}

sealed interface SubmitState {
    object Idle : SubmitState

    object Loading : SubmitState

    data class Success(
        val serviceId: Long,
    ) : SubmitState

    data class Error(
        val message: String,
    ) : SubmitState

    data object Dismiss : SubmitState
}
