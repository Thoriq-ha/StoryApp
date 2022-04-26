package com.thor.latihancleancode.ui.main.upload

import com.thor.latihancleancode.data.remote.response.FileUploadResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UploadUseCase {
    fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
    ): Observable<FileUploadResponse>
}