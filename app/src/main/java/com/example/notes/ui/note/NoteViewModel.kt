package com.example.notes.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.notes.data.NotesRepository
import com.example.notes.data.entity.Note
import com.example.notes.data.model.NoteResult
import com.example.notes.ui.base.BaseViewModel

class NoteViewModel: BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var noteLiveData : LiveData<NoteResult>? = null
    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when(result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(result.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            noteLiveData?.removeObserver(this)
        }
    }

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(id: String) {
        noteLiveData = NotesRepository.getNoteById(id)
        noteLiveData?.observeForever(noteObserver)
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
        noteLiveData?.removeObserver(noteObserver)
    }
}