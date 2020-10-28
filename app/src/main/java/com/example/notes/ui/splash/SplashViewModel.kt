package com.example.notes.ui.splash

import android.provider.ContactsContract
import com.example.notes.data.NotesRepository
import com.example.notes.data.errors.NoAuthException
import com.example.notes.ui.base.BaseViewModel

class SplashViewModel: BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let { SplashViewState(true) } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}