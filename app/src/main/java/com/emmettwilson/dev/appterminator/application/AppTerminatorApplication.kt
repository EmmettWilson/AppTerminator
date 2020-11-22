package com.emmettwilson.dev.appterminator.application

import android.app.Application
import com.emmettwilson.dev.appterminator.wiring.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@Suppress("unused")
class AppTerminatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@AppTerminatorApplication)
            modules(appModule)
        }
    }
}
