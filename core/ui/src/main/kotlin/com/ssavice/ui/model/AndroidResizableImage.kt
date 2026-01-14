package com.ssavice.ui.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import com.ssavice.model.ResizableImage
import java.io.ByteArrayOutputStream

data class AndroidResizableImage(
    override val data: ByteArray,
    override val mimeType: String,
    val width: Int? = null,
    val height: Int? = null,
) : ResizableImage {
    /**
     * 이미지를 특정 크기로 줄이고 JPG로 재인코딩합니다.
     * @param targetWidth 목표 가로 크기
     * @param quality JPG 압축 품질 (0-100)
     */
    fun compressAndResize(
        targetWidth: Int,
        quality: Int = 80,
    ): AndroidResizableImage {
        // 1. ByteArray -> Bitmap 변환
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

        // 2. 리사이징 계산
        val aspectRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
        val targetHeight = (targetWidth * aspectRatio).toInt()

        val scaledBitmap = bitmap.scale(targetWidth, targetHeight)

        // 3. JPG로 재인코딩 (PNG -> JPG 변환도 여기서 발생)
        val outputStream =
            ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        return this.copy(
            data = outputStream.toByteArray(),
            mimeType = "image/jpeg", // 인코딩 결과 반영
            width = targetWidth,
            height = targetHeight,
        )
    }

    fun compressToTargetSize(
        targetSizeInBytes: Long,
        minQuality: Int = 70,
    ): AndroidResizableImage {
        var currentBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        var currentQuality = 90
        var currentData = data
        var currentWidth = currentBitmap.width

        val stream = ByteArrayOutputStream()
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, stream)
        currentData = stream.toByteArray()

        while (currentData.size > targetSizeInBytes && currentQuality >= minQuality) {
            val stream = ByteArrayOutputStream()
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, stream)
            currentData = stream.toByteArray()
            currentQuality -= 10
        }

        while (currentData.size > targetSizeInBytes && currentWidth > 100) {
            currentWidth = (currentWidth * 0.8).toInt()
            val aspectRatio = currentBitmap.height.toFloat() / currentBitmap.width.toFloat()
            val targetHeight = (currentWidth * aspectRatio).toInt()

            val scaledBitmap = currentBitmap.scale(currentWidth, targetHeight)

            val stream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, minQuality, stream)
            currentData = stream.toByteArray()
            currentBitmap = scaledBitmap
        }

        return this.copy(
            data = currentData,
            mimeType = "image/jpeg",
            width = currentBitmap.width,
            height = currentBitmap.height,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ResizableImage
        return data.contentEquals(other.data) && mimeType == other.mimeType
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}
