package com.ssavice.ui.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.graphics.scale
import com.ssavice.model.ResizableImage
import java.io.ByteArrayInputStream
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

    fun compressToTargetSize(
        targetSizeInBytes: Long,
        minQuality: Int = 70,
    ): AndroidResizableImage {
        var currentBitmap =
            BitmapFactory
                .decodeByteArray(data, 0, data.size)
                .rotateIfRequired(data)
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

    private fun Bitmap.rotateIfRequired(data: ByteArray): Bitmap {
        val inputStream = ByteArrayInputStream(data)
        val exif = androidx.exifinterface.media.ExifInterface(inputStream)
        val orientation =
            exif.getAttributeInt(
                androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL,
            )

        val degree =
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> return this
            }

        val matrix = Matrix().apply { postRotate(degree) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
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

    companion object {
        fun fromUri(uri: Uri,
                    context: Context,
                    targetSizeInBytes: Long = Long.MAX_VALUE,
        ): AndroidResizableImage? {
            val inputStream = context.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()

            if (byteArray == null) {
                return null
            }

            return AndroidResizableImage(byteArray, "", null, null)
                .compressToTargetSize(targetSizeInBytes)
        }
    }
}
