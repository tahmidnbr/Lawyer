package com.example.lawyer

import android.app.Application
import com.example.lawyer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RootApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RootApplication)
            modules(appModule)
        }
    }
}