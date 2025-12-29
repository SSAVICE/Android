package com.ssavice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage


enum class ServiceStatus(val label: String) {
    IN_PROGRESS("진행중"),
    RECRUITING("모집완료")
}

@Composable
fun SellerServiceListItem(
    title: String,
    category: String,
    meta: String, // 예: "8/12명" 또는 "10/15명"
    priceText: String, // 예: "₩400,000"
    status: ServiceStatus,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 14.dp,
    elevation: Dp = 8.dp,
    onClick: ((Int) ->  Unit)? = null,
    thumbnail: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        Row(
            modifier = modifier
                // 그림자 + round rect (clip 전에 shadow가 오도록)
                .shadow(elevation = elevation, shape = shape, clip = false)
                .clip(shape)
                .background(Color.White)
                .fillMaxWidth()
                .clickable(true) { onClick }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF2F2F2))
            ) {
                thumbnail()
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111111),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
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
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = meta,
                        fontSize = 12.sp,
                        color = Color(0xFF8A8A8A)
                    )
                    Text(
                        text = priceText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: ServiceStatus,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (status) {
        ServiceStatus.IN_PROGRESS -> Color(0xFFE9F7EF) to Color(0xFF1E8E3E) // 진행중(연녹/진녹)
        ServiceStatus.RECRUITING -> Color(0xFFEFF1F3) to Color(0xFF6B7280)   // 모집완료(회색)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = fg
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
        status = ServiceStatus.IN_PROGRESS,
        thumbnail = {
            AsyncImage(
                model = yogaImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

private const val yogaImage
= "https://images.unsplash.com/photo-1552196527-bffef41ef674?q=80&w=2226&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
