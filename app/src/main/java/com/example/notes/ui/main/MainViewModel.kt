package com.example.notes.ui.main

import androidx.lifecycle.Observer
import com.example.notes.data.NotesRepository
import com.example.notes.data.entity.Note
import com.example.notes.data.model.NoteResult
import com.example.notes.ui.base.BaseViewModel

class MainViewModel (val notesRepository: NotesRepository): BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer {result: NoteResult? ->
        result?: return@Observer
        when(result) {
            is NoteResult.Success<*> ->
                viewStateLiveData.value = MainViewState(result.data as? List<Note>)
            is NoteResult.Error ->
                viewStateLiveData.value = MainViewState(error = result.error)
        }
    }

    private val mainLiveData = notesRepository.getNotes()

    init {
        mainLiveData.observeForever(notesObserver)
    }

    override fun onCleared() {
        super.onCleared()
        mainLiveData.removeObserver(notesObserver)
    }


}
