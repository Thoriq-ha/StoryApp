package com.thor.latihancleancode.ui.main.home

import com.thor.latihancleancode.repository.story.Story
import io.reactivex.Observable
interface HomeUseCase {


    fun list(token: String): Observable<List<Story>>

}