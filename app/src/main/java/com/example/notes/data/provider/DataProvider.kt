package com.example.notes.data.provider

import androidx.lifecycle.LiveData
import com.example.notes.data.entity.Note
import com.example.notes.data.model.NoteResult

interface DataProvider {
    fun getNotes(): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>

}