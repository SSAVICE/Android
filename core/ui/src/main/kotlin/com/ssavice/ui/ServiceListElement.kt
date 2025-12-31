package com.ssavice.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.component.SsaviceBackground
import com.ssavice.designsystem.theme.SsaviceTheme
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ServiceListElement(
    id: Long,
    imageUrl: String,
    sellerName: String,
    sellerThumbnailUrl: String,
    serviceName: String,
    tags: List<String>,
    locationInfo: String,
    deadline: String,
    price: Int,
    discountedPrice: Int,
    participationInfo: String,
    onServiceClick: (Long) -> Unit,
    thumbnail: @Composable (String) -> Unit = {}
) {
    val discountRate = ((price - discountedPrice).toFloat() / price * 100).roundToInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onServiceClick(id) },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier.size(120.dp)
            ) {
                thumbnail(imageUrl)
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .height(34.dp)
                        .wrapContentWidth()
                        .padding(4.dp),
                    color = MaterialTheme.colorScheme.surfaceTint,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "$discountRate%",
                        color = Color.White,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            .width(30.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.wrapContentHeight()
            ) {
                Text(text = sellerName, fontSize = 10.sp, color = Color.Gray)

                Text(
                    text = serviceName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .height(height = 20.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 9.sp,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 0.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = locationInfo, fontSize = 9.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Deadline",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = deadline, fontSize = 9.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "₩${discountedPrice.format()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "₩${price.format()}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Participants",
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = participationInfo,
                            fontSize = 9.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

private fun Int.format(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

@Preview(showBackground = true)
@Composable
private fun ServiceListElementPreview() {
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(height = 720.dp, width = 540.dp)) {
            Column {
                ServiceListElement(
                    id = 1L,
                    imageUrl = YOGA_IMAGE_URL,
                    sellerName = "힐링요가스튜디오",
                    sellerThumbnailUrl = "",
                    serviceName = "주말 요가 클래스",
                    tags = listOf("요가", "힐링"),
                    locationInfo = "강남구 · 0.5km",
                    deadline = "2일 후 마감",
                    price = 70000,
                    discountedPrice = 50000,
                    participationInfo = "8/15명 참여중",
                    onServiceClick = {}
                ) { url ->
                    AsyncImage(
                        model =
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .build(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    )
                }
                ServiceListElement(
                    id = 1L,
                    imageUrl = YOGA_IMAGE_URL,
                    sellerName = "힐링요가스튜디오",
                    sellerThumbnailUrl = "",
                    serviceName = "주말 요가 클래스",
                    tags = listOf("요가", "힐링"),
                    locationInfo = "강남구 · 0.5km",
                    deadline = "2일 후 마감",
                    price = 70000,
                    discountedPrice = 50000,
                    participationInfo = "8/15명 참여중",
                    onServiceClick = {}
                )
                ServiceListElement(
                    id = 1L,
                    imageUrl = YOGA_IMAGE_URL,
                    sellerName = "힐링요가스튜디오",
                    sellerThumbnailUrl = "",
                    serviceName = "주말 요가 클래스",
                    tags = listOf("요가", "힐링"),
                    locationInfo = "강남구 · 0.5km",
                    deadline = "2일 후 마감",
                    price = 70000,
                    discountedPrice = 50000,
                    participationInfo = "8/15명 참여중",
                    onServiceClick = {}
                )
            }
        }
    }
}

private const val YOGA_IMAGE_URL =
    "https://images.unsplash.com/photo-1552196527-bffef41ef674"
