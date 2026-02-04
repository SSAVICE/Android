package com.ssavice.mappicker

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.webkit.WebViewAssetLoader
import com.ssavice.mappicker.WebviewConstants.DOMAIN
import com.ssavice.mappicker.WebviewConstants.PATH
import com.ssavice.mappicker.core.FileWebViewClient
import com.ssavice.mappicker.core.JavascriptInterface
import com.ssavice.mappicker.datasource.ConvertCoordinateDatasource
import com.ssavice.mappicker.model.AddressPickResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AddressPickerWebView(
    modifier: Modifier = Modifier,
    onResult: (AddressPickResult) -> Unit,
    viewModel: AddressPickerViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background

    LaunchedEffect(viewModel, lifecycle) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.coordinateConvertFinishedEvent.collect { result ->
                onResult(result)
            }
        }
    }

    AndroidView(
        modifier = modifier.background(Color.Green),
        factory = {
            val webView = WebView(it)
            webView.settings.apply {
                javaScriptEnabled = true
                allowFileAccess = false
                allowContentAccess = false
            }
            webView.addJavascriptInterface(
                JavascriptInterface(onResult = viewModel::convertCoordinate),
                WebviewConstants.JS_BRIDGE,
            )
            webView.setBackgroundColor(backgroundColor.toArgb())

            val assetLoader =
                WebViewAssetLoader
                    .Builder()
                    .addPathHandler(
                        "/$PATH/",
                        WebViewAssetLoader.AssetsPathHandler(context),
                    ).setDomain(DOMAIN)
                    .build()

            webView.webViewClient = FileWebViewClient(assetLoader)

            webView
        },
        update = {
            it.loadUrl("https://$DOMAIN/$PATH/html/addressPicker.html")
        },
    )
}

object WebviewConstants {
    internal const val JS_BRIDGE = "address_finder" // Javascript 와 통신하기 위한 브릿지 프로토콜
    internal const val DOMAIN = "address.finder.net" // 로컬 가상 도메인
    internal const val PATH = "assets"

    const val TAG = "AddressPickerWebView"
}
