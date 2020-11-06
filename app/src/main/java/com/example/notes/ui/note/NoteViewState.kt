package com.example.notes.ui.note

import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    class Data(val note: Note? = null, val isDeleted: Boolean = false)
}