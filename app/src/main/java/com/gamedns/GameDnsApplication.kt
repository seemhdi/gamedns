package com.gamedns

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GameDnsApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize app-level components here
    }
}
