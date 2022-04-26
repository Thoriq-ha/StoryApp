package com.thor.storyapp.ui.main.home

import androidx.lifecycle.MutableLiveData
import com.thor.storyapp.base.BaseViewModel
import com.thor.storyapp.repository.story.Story
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val useCase: HomeUseCase) : BaseViewModel() {
    private val _stateList = MutableLiveData<HomeState>()

    val stateList get() = _stateList

    fun refresh() {
        getList(token)
    }

    private fun getList(token: String?) {
        useCase.list(token ?: "")
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _stateList.postValue(HomeState.OnLoading)
            }.subscribe({
                _stateList.postValue(HomeState.OnSuccess(it))
            }, {
                _stateList.postValue(HomeState.OnError(it?.message ?: "Terjadi Kesalahan"))
            }).disposeOnCleared()
    }
}

sealed class HomeState {
    object OnLoading : HomeState()
    data class OnSuccess(val list: List<Story>) : HomeState()
    data class OnError(val message: String) : HomeState()
}