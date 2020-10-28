package com.example.notes.data

import com.example.notes.data.entity.Note
import com.example.notes.data.provider.DataProvider
import com.example.notes.data.provider.FirestoreDataProvider

object NotesRepository {

    val dataProvider: DataProvider = FirestoreDataProvider()
    fun getNotes() = dataProvider.getNotes()

    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun getCurrentUser() = dataProvider.getCurrentUser()
}