package com.thor.latihancleancode.base

import android.app.Application
import com.thor.latihancleancode.BuildConfig
import com.thor.latihancleancode.data.preferences.preferencesModul
import com.thor.latihancleancode.data.remote.errorHandleModule
import com.thor.latihancleancode.data.remote.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

abstract class BaseApplication: Application() {
    abstract fun defineDependencies(): List<Module>
    override fun onCreate() {
        super.onCreate()
        dependenciesInjection()
    }

    private fun dependenciesInjection() {
        startKoin {

            androidLogger( if (BuildConfig.DEBUG) Level.ERROR else Level.NONE )

            androidContext(this@BaseApplication)

            modules(
                mutableListOf(remoteModule, preferencesModul, errorHandleModule)
                    .apply { addAll(defineDependencies()) }
            )
        }
    }
}