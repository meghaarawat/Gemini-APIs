package com.kitlabs.aiapp.base

import android.app.Application
import androidx.lifecycle.LifecycleObserver

class App : Application(), LifecycleObserver {
    companion object {
        lateinit var instance: App

        @JvmStatic
        fun get(): App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}