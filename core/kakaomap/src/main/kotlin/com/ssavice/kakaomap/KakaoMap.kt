package com.ssavice.kakaomap

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

@Composable
fun KakaoMap(
    modifier: Modifier = Modifier,
    longitude: Double,
    latitude: Double,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) } // KakaoMapView를 기억하여 재사용할 수 있도록 설정
    var mapLoaded by remember { mutableStateOf(false) }

    AndroidView(
        modifier = modifier.height(200.dp), // AndroidView의 높이 임의 설정
        factory = { context ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                        }

                        override fun onMapError(exception: Exception?) {
                        }
                    },
                    object : KakaoMapReadyCallback() {

                        override fun onMapReady(kakaoMap: KakaoMap) {

                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))

                            val style = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(

                            )))

                            val options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(style)

                            val layer = kakaoMap.labelManager?.layer


                            kakaoMap.moveCamera(cameraUpdate)

                            // 지도에 라벨을 추가
                            layer?.addLabel(options)

                            mapLoaded = true
                        }

                        override fun getPosition(): LatLng {
                            return LatLng.from(latitude, longitude)
                        }
                    },
                )
            }
        },
    )
}
