package com.thor.latihancleancode.ui.auth.login

import com.thor.latihancleancode.data.remote.response.LoginResponse
import io.reactivex.Observable

interface LoginUseCase {
    fun login(
        email: String,
        password: String
    ): Observable<LoginResponse>

}