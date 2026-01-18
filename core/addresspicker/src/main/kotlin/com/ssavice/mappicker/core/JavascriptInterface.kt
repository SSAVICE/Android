package com.ssavice.mappicker.core

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import com.ssavice.mappicker.model.AddressPickResult

internal class JavascriptInterface(
    private val onResult: (AddressPickResult) -> Unit,
) {
    @android.webkit.JavascriptInterface
    fun result(
        address: String,
        zipCode: String,
        bcode: String,
    ) {
        onResult(AddressPickResult(address, zipCode, bcode))
    }

    companion object {
        const val ADDRESS = "address"
        const val ZIPCODE = "zipcode"
        const val REGIONCODE = "bcode"
    }
}
