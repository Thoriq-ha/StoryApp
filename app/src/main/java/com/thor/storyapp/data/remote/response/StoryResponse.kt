package com.thor.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.thor.storyapp.repository.story.Story
import io.reactivex.Observable

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("listStory")
    val listStory: List<Story> = emptyList()
)

fun StoryResponse.mapToList() = Observable.just(this.listStory)
