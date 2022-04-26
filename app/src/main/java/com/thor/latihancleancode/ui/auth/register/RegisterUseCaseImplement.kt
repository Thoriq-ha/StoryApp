package com.thor.latihancleancode.ui.auth.register

import com.thor.latihancleancode.data.remote.response.RegisterResponse
import com.thor.latihancleancode.repository.auth.AuthRepository
import io.reactivex.Observable

class RegisterUseCaseImplement(private val repository: AuthRepository) : RegisterUseCase {
    override fun register(
        name: String,
        email: String,
        password: String
    ): Observable<RegisterResponse> =
        repository.register(name, email, password)
}
