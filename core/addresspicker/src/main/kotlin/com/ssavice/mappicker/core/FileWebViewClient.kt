package com.ssavice.mappicker.core

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat

internal class FileWebViewClient(
    private val assetLoader: WebViewAssetLoader,
) : WebViewClientCompat() {
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?,
    ): WebResourceResponse? = assetLoader.shouldInterceptRequest(request!!.url)
}
