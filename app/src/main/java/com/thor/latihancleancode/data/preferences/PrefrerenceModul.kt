package com.thor.latihancleancode.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.thor.latihancleancode.data.preferences.datastore.DataStoreSession
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private val Context.dataStoreUserSession by preferencesDataStore(name = "setting_session.pref")

val preferencesModul = module {
    single {
        DataStoreSession(androidApplication().dataStoreUserSession)
    }

}