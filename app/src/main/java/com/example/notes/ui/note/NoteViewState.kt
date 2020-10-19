package com.example.notes.ui.note

import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseViewState

class NoteViewState(note : Note? = null, error: Throwable? = null)
    : BaseViewState<Note?>(note, error){
}