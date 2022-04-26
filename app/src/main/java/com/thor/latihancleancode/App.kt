@file:Suppress("unused")

package com.thor.latihancleancode
import com.thor.latihancleancode.base.BaseApplication
import com.thor.latihancleancode.data.remote.serviceModule
import com.thor.latihancleancode.repository.repositoryModule
import com.thor.latihancleancode.ui.viewModelModule
import org.koin.core.module.Module

class App: BaseApplication() {
    override fun defineDependencies(): List<Module> {
        return listOf(
            serviceModule,
            repositoryModule,
            viewModelModule
        )
    }
}