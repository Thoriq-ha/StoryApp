package com.thor.latihancleancode.data.remote.services

import com.thor.latihancleancode.data.remote.response.FileUploadResponse
import com.thor.latihancleancode.data.remote.response.StoryResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryServices {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("stories")
    fun stories(@Header("Authorization") token: String): Observable<StoryResponse>


    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Observable<FileUploadResponse>

}