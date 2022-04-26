package com.thor.storyapp.repository.story

import com.thor.storyapp.data.remote.services.StoryServices
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val service: StoryServices) {
    fun list(token: String) = service.stories(token)

    fun upload(token: String, file: MultipartBody.Part, description: RequestBody) =
        service.uploadImage(token, file, description)
}