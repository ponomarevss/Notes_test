package com.example.notes.ui.note

import androidx.lifecycle.ViewModel
import com.example.notes.data.Repository
import com.example.notes.data.entity.Note

class NoteViewModel(): ViewModel() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            Repository.saveNote(it)
        }
    }
}