package com.example.cakecrave

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.google.firebase.FirebaseApp

class App : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    // ================= COIL 3 IMAGE LOADER =================
    // Required so AsyncImage can load remote URLs (Cloudinary)
    override fun newImageLoader(context: coil3.PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory())
            }
            .crossfade(true)
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }

    init {
        instance = this
    }
}
