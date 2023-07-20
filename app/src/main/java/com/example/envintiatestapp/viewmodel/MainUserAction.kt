package com.example.envintiatestapp.viewmodel

import com.example.envintiatestapp.model.CapturedText

sealed interface MainUserAction {
    data class TextSubmittedSuccess(val text: CapturedText) : MainUserAction
    data class TextSubmittedError(val throwable: Throwable) : MainUserAction
    object AwaitingTextSubmission : MainUserAction
}