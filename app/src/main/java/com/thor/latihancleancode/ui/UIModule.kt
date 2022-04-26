package com.thor.latihancleancode.ui

import com.thor.latihancleancode.ui.auth.login.LoginUseCase
import com.thor.latihancleancode.ui.auth.login.LoginUseCaseImplement
import com.thor.latihancleancode.ui.auth.login.LoginViewModel
import com.thor.latihancleancode.ui.auth.register.RegisterUseCase
import com.thor.latihancleancode.ui.auth.register.RegisterUseCaseImplement
import com.thor.latihancleancode.ui.auth.register.RegisterViewModel
import com.thor.latihancleancode.ui.main.home.HomeUseCase
import com.thor.latihancleancode.ui.main.home.HomeUseCaseImplement
import com.thor.latihancleancode.ui.main.home.HomeViewModel
import com.thor.latihancleancode.ui.main.upload.UploadUseCase
import com.thor.latihancleancode.ui.main.upload.UploadUseCaseImplement
import com.thor.latihancleancode.ui.main.upload.UploadViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<HomeUseCase> { HomeUseCaseImplement(get()) }
    viewModel { HomeViewModel(get()) }

    single<UploadUseCase> { UploadUseCaseImplement(get()) }
    viewModel { UploadViewModel(get()) }

    single<RegisterUseCase> { RegisterUseCaseImplement(get()) }
    viewModel { RegisterViewModel(get()) }

    single<LoginUseCase> { LoginUseCaseImplement(get()) }
    viewModel { LoginViewModel(get()) }
}