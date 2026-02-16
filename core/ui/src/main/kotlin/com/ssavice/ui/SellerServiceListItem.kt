package com.ssavice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ssavice.model.enums.ServiceState

@Composable
fun SellerServiceListItem(
    title: String,
    category: String,
    meta: String, // 예: "8/12명" 또는 "10/15명"
    priceText: String, // 예: "₩400,000"
    status: ServiceState,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 14.dp,
    elevation: Dp = 8.dp,
    onClick: ((Long) -> Unit)? = null,
    thumbnail: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = Modifier.background(Color.Transparent),
    ) {
        Row(
            modifier =
                modifier
                    // 그림자 + round rect (clip 전에 shadow가 오도록)
                    .shadow(elevation = elevation, shape = shape, clip = false)
                    .clip(shape)
                    .background(Color.White)
                    .fillMaxWidth()
                    .clickable(true) { onClick }
                    .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF2F2F2)),
            ) {
                thumbnail()
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111111),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    StatusChip(status = status)
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = category,
                    fontSize = 12.sp,
                    color = Color(0xFF8A8A8A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = meta,
                        fontSize = 12.sp,
                        color = Color(0xFF8A8A8A),
                    )
                    Text(
                        text = priceText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: ServiceState,
    modifier: Modifier = Modifier,
) {
    val (bg, fg) =
        when (status) {
            ServiceState.RECRUITING -> Color(0xFFFFF3E0) to Color(0xFFE65100)

            ServiceState.SUCCEEDED,
            ServiceState.COMPLETED,
            ServiceState.ALL,
            -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)

            ServiceState.CANCELED,
            ServiceState.USER_CANCELED,
            ServiceState.FAILED,
            -> Color(0xFFF5F5F5) to Color(0xFF757575)

            ServiceState.UNKNOWN,
            -> Color(0xFFFF8B8B) to Color(0xFFFF3434)
        }

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(999.dp))
                .background(bg)
                .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = status.value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = fg,
        )
    }
}

/** 사용 예시 */
@Preview
@Composable
fun PreviewLikeItem() {
    SellerServiceListItem(
        title = "요가 레슨",
        category = "운동/피트니스",
        meta = "8/12명",
        priceText = "₩400,000",
        status = ServiceState.RECRUITING,
        thumbnail = {
            AsyncImage(
                model = YOGA_IMAGE_URL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        },
    )
}

private const val YOGA_IMAGE_URL =
    "https://images.unsplash.com/photo-1552196527-bffef41ef674?q=80&w=2226&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
