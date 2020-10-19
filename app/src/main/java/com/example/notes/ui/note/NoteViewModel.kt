package com.example.notes.ui.note

import androidx.lifecycle.ViewModel
import com.example.notes.data.NotesRepository
import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseViewModel

class NoteViewModel(): BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}