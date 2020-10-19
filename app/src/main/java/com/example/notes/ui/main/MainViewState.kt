package com.example.notes.ui.main

import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null)
    : BaseViewState<List<Note>?>(notes, error)
