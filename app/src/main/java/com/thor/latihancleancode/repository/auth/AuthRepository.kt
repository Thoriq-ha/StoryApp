package com.thor.latihancleancode.repository.auth

import com.thor.latihancleancode.data.remote.services.AuthServices

class AuthRepository(private val service: AuthServices) {
    fun login(email: String, password: String) = service.login(email, password)

    fun register(name: String, email: String, password: String) =
        service.register(name, email, password)
}