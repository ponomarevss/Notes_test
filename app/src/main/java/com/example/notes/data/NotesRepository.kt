package com.example.notes.data

import androidx.lifecycle.MutableLiveData
import com.example.notes.data.entity.Note
import java.util.*

object NotesRepository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes = mutableListOf(
        Note(
            UUID.randomUUID().toString(),
            "Первая заметка",
            "Текст первой заметки. Весьма кортокий, но от этого нисколько не теряющий своей важности",
            Note.Color.WHITE
        ),
        Note(
            UUID.randomUUID().toString(),
            "Вторая заметка",
            "Текст второй заметки. Весьма кортокий, но от этого нисколько не теряющий своей важности",
            Note.Color.BLUE
        ),
        Note(
            UUID.randomUUID().toString(),
            "Третья заметка",
            "Текст третьтей заметки. Весьма кортокий, но от этого нисколько не теряющий своей важности",
            Note.Color.GREEN
        ),
        Note(
            UUID.randomUUID().toString(),
            "Четвертая заметка",
            "Текст четвертой заметки. Весьма кортокий, но от этого нисколько не теряющий своей важности",
            Note.Color.RED
        ),
        Note(
            UUID.randomUUID().toString(),
            "Пятая заметка",
            "Текст пятой заметки. Весьма кортокий, но от этого нисколько не теряющий своей важности",
            Note.Color.VIOLET
        ),
        Note(
            UUID.randomUUID().toString(),
            "Шестая заметка",
            "Текст шестой заметки. Весьма кортокий, но от этого нисколько не теряющий своей важности",
            Note.Color.YELLOW
        )
    )

    init{
        notesLiveData.value = notes
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes

    }

    private fun addOrReplace(note: Note) {
        for (i in notes.indices) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

    fun getNotes() = notesLiveData
}