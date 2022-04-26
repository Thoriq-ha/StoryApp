package com.thor.latihancleancode.data.remote.response

import com.google.gson.annotations.SerializedName
import com.thor.latihancleancode.repository.auth.Login

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("loginResult")
    val loginResult: Login,
)

