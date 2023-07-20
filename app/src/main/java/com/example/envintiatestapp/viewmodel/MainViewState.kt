package com.example.envintiatestapp.viewmodel

import com.example.envintiatestapp.model.CapturedText

sealed interface MainViewState {
    object AwaitingLoading : MainViewState
    object Loading : MainViewState
    data class LoadingError(val throwable: Throwable) : MainViewState
    data class LoadingSuccess(val texts: List<CapturedText>) : MainViewState
}