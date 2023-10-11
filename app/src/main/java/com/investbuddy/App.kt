package com.investbuddy

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        resource = resources

    }

    companion object {
        lateinit var resource: Resources
    }
}
