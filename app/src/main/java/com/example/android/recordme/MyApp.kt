package com.example.android.recordme

import android.app.Application
import android.content.Context

class MyApp : Application() {
    private lateinit var application: Application

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    fun getMyAppContext(): Context {
        return application.applicationContext
    }
}