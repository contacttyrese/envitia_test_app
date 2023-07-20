package com.example.envintiatestapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.envintiatestapp.model.CapturedText
import com.example.envintiatestapp.model.TextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TextRepository,
    private val disposables: CompositeDisposable,
) : ViewModel() {
    private val _viewState = MutableLiveData<MainViewState>()
    val viewState: LiveData<MainViewState>
        get() = _viewState
    val userActionSubject = PublishSubject.create<MainUserAction>()

    init {
        _viewState.postValue(MainViewState.AwaitingLoading)
        if (repository.capTexts.size > 0) {
           _viewState.postValue(MainViewState.LoadingSuccess(repository.capTexts))
        }
        disposables.add(
            userActionSubject.subscribe({ mainUserAction ->
                onUserAction(mainUserAction)
            }, { throwable ->
                processThrowable(throwable)
            })
        )
    }

    private fun onUserAction(mainUserAction: MainUserAction) {
        when (mainUserAction) {
            is MainUserAction.AwaitingTextSubmission -> {
                Log.i("user_action_awaiting", "text submit awaiting")
                _viewState.postValue(MainViewState.AwaitingLoading)
            }
            is MainUserAction.TextSubmittedError -> {
                Log.e("user_action_error", "text submit error: ${mainUserAction.throwable}")
                processThrowable(mainUserAction.throwable)
            }
            is MainUserAction.TextSubmittedSuccess -> {
                _viewState.postValue(MainViewState.Loading)
                Log.i("user_action_success", "text submit success")
                repository.saveTextToJson(mainUserAction.text)
                disposables.add(
                    repository.getLoadedTexts().subscribe({ texts ->
                        processText(texts)
                    }, { throwable ->
                        processThrowable(throwable)
                    })
                )
            }
        }
    }

    private fun processThrowable(throwable: Throwable) {
        Log.e("process_text_error", "text processing error $throwable")
        throwable.printStackTrace()
        _viewState.postValue(MainViewState.LoadingError(throwable))
    }

    private fun processText(texts: List<CapturedText>) {
        Log.i("process_text_sub", "size of texts is ${texts.size}")
        _viewState.postValue(MainViewState.LoadingSuccess(texts))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}