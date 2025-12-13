package com.ssavice.ssavice

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SsaviceApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
