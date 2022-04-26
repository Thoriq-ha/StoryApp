package com.thor.storyapp.ui.main.home

import com.thor.storyapp.repository.story.Story
import io.reactivex.Observable
interface HomeUseCase {


    fun list(token: String): Observable<List<Story>>

}