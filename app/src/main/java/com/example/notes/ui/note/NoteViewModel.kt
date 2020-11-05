package com.example.notes.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.notes.data.NotesRepository
import com.example.notes.data.entity.Note
import com.example.notes.data.model.NoteResult
import com.example.notes.ui.base.BaseViewModel

class NoteViewModel(val notesRepository: NotesRepository): BaseViewModel<NoteViewState.Data?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var noteLiveData : LiveData<NoteResult>? = null
    private var deleteLiveData : LiveData<NoteResult>? = null
    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when(result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(NoteViewState.Data(result.data as? Note))
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            noteLiveData?.removeObserver(this)
        }
    }

    private val deleteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when(result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(NoteViewState.Data(isDeleted = true))
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            deleteLiveData?.removeObserver(this)
        }
    }

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(id: String) {
        noteLiveData = notesRepository.getNoteById(id)
        noteLiveData?.observeForever(noteObserver)
    }

    fun deleteNote() {
        pendingNote?.let {
            deleteLiveData = notesRepository.deleteNote(it.id)
            deleteLiveData?.observeForever(deleteObserver)
        }
    }

    override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
        noteLiveData?.removeObserver(noteObserver)
    }
}