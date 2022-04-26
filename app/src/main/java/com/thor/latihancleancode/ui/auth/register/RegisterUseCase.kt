package com.thor.latihancleancode.ui.auth.register

import com.thor.latihancleancode.data.remote.response.RegisterResponse
import io.reactivex.Observable

interface RegisterUseCase {

    fun register(
        name: String,
        email: String,
        password: String
    ): Observable<RegisterResponse>
}