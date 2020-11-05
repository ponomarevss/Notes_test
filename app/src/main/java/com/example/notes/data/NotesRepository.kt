package com.example.notes.data

import com.example.notes.data.entity.Note
import com.example.notes.data.provider.DataProvider

class NotesRepository(val dataProvider: DataProvider ) {
    fun getNotes() = dataProvider.getNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun deleteNote(id: String) = dataProvider.deleteNote(id)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun getCurrentUser() = dataProvider.getCurrentUser()
}