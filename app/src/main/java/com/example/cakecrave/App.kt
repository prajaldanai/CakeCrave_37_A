package com.example.cakecrave

import android.app.Application
import com.google.firebase.FirebaseApp

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var instance: App
            private set
    }

    init {
        instance = this
    }
}
