package com.thor.storyapp.ui.main.home

import com.thor.storyapp.data.remote.response.mapToList
import com.thor.storyapp.repository.story.Story
import com.thor.storyapp.repository.story.StoryRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class HomeUseCaseImplement(private val repository: StoryRepository) : HomeUseCase {

    override fun list(token: String): Observable<List<Story>> {
        return repository.list(token)
            .subscribeOn(Schedulers.io())
            .flatMap { response -> response.mapToList() }
    }
}