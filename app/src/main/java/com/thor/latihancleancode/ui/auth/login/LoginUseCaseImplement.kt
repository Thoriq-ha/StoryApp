package com.thor.latihancleancode.ui.auth.login

import com.thor.latihancleancode.data.remote.response.LoginResponse
import com.thor.latihancleancode.repository.auth.AuthRepository
import io.reactivex.Observable

class LoginUseCaseImplement(private val repository: AuthRepository) : LoginUseCase {
    override fun login(email: String, password: String): Observable<LoginResponse> {
        return repository.login(email, password)
    }
}