package com.ssavice.service_detail.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.unit.dp
import com.ssavice.ui.AsyncImageScrollList

@Composable
fun ServiceImagesWithButtons(
    urls: List<String>,
    onLikeClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onImageClick: (String) -> Unit = {},
    liked: Boolean = false,
    showButtons: Boolean = true,
) {
    val containerColor by animateColorAsState(
        targetValue = if (liked) Color(0xFFE91E63) else MaterialTheme.colorScheme.surface,
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
        label = "LikeContainerColor",
    )

    val contentColor by animateColorAsState(
        targetValue = if (liked) Color.White else MaterialTheme.colorScheme.onSurface,
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
        label = "LikeContentColor",
    )

    Box {
        AsyncImageScrollList(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            imageUrls = urls,
            onImageClick = onImageClick,
        )
        if (showButtons) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
            ) {
                IconButton(
                    modifier =
                        Modifier
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                spotColor = DefaultShadowColor.copy(alpha = 0.4f),
                                ambientColor = DefaultShadowColor.copy(alpha = 0.4f),
                            ).clip(CircleShape),
                    onClick = onLikeClick,
                    colors =
                        IconButtonDefaults.filledIconButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColor,
                        ),
                ) {
                    Icon(
                        imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = contentColor,
                    )
                }
                IconButton(
                    modifier =
                        Modifier
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                spotColor = DefaultShadowColor.copy(alpha = 0.4f),
                                ambientColor = DefaultShadowColor.copy(alpha = 0.4f),
                            ).clip(CircleShape),
                    onClick = onShareClick,
                    colors =
                        IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
