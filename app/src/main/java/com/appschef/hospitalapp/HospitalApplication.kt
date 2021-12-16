package com.appschef.hospitalapp

import android.app.Application
import com.appschef.hospitalapp.util.InternetService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HospitalApplication: Application() {
    companion object {
        lateinit var instance: HospitalApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        setupServices()
    }

    private fun setupServices() {
        InternetService.instance.initializeWithApplicationContext(this)
    }
}