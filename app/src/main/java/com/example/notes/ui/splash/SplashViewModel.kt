package com.example.notes.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.notes.data.NotesRepository
import com.example.notes.data.entity.User
import com.example.notes.data.errors.NoAuthException
import com.example.notes.ui.base.BaseViewModel

class SplashViewModel: BaseViewModel<Boolean?, SplashViewState>() {

    private var userLiveData : LiveData<User?>? = null
    private val userObserver = object : Observer<User?> {
        override fun onChanged(t: User?) {
            viewStateLiveData.value = t?.let { SplashViewState(true) } ?: let {
                SplashViewState(error = NoAuthException())
            }
            userLiveData?.removeObserver(this)
        }

    }

    fun requestUser() {
        userLiveData = NotesRepository.getCurrentUser()
        userLiveData?.observeForever(userObserver)
    }
}