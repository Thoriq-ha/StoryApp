package com.thor.latihancleancode.ui.main.upload

import com.thor.latihancleancode.data.remote.response.FileUploadResponse
import com.thor.latihancleancode.repository.story.StoryRepository
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadUseCaseImplement(private val repository: StoryRepository) : UploadUseCase {
    override fun upload(
        token : String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Observable<FileUploadResponse> =
        repository.upload(token, file, description)
}