package com.ssavice.ui.uploadImage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.model.ImageUploadProgress

@Composable
fun ImageWithUploadState(
    baseImageUrl: String? = null,
    uploadingImageUrl: String? = null,
    uploadState: ImageUploadProgress,
    contentDescription: String,
) {
    val url =
        if (baseImageUrl == null) {
            uploadingImageUrl
        } else {
            if (uploadingImageUrl == null) {
                baseImageUrl
            } else {
                if (uploadState is ImageUploadProgress.Waiting) {
                    baseImageUrl
                } else {
                    uploadingImageUrl
                }
            }
        }
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
        )
        if (isProcessing(uploadState)) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
            ) {
                val text =
                    when (uploadState) {
                        is ImageUploadProgress.Done -> {
                            "완료"
                        }

                        is ImageUploadProgress.Error -> {
                            "${uploadState.throwable.message}"
                        }

                        is ImageUploadProgress.Preprocessing -> {
                            "전처리 중"
                        }

                        is ImageUploadProgress.Progress -> {
                            "업로드 중 (${uploadState.progressPercentile}%)"
                        }

                        ImageUploadProgress.Waiting -> {
                            ""
                        }
                    }

                Text(
                    text = text,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    maxLines = 3,
                )
            }
        }
    }
}

private fun isProcessing(uploadState: ImageUploadProgress): Boolean =
    (
        uploadState !is ImageUploadProgress.Waiting &&
            uploadState !is ImageUploadProgress.Done
    )
