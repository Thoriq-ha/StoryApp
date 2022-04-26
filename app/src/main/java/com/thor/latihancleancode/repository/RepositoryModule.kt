package com.thor.latihancleancode.repository

import com.thor.latihancleancode.repository.auth.AuthRepository
import com.thor.latihancleancode.repository.story.StoryRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { StoryRepository(get()) }
    single { AuthRepository(get()) }
}