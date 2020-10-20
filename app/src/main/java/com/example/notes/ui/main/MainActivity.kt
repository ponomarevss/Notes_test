package com.example.notes.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notes.R
import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseActivity
import com.example.notes.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override val layoutResource = R.layout.activity_main
    lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notes_rv.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRVAdapter{
            NoteActivity.start(this, it.id)
        }

        notes_rv.adapter = adapter

        fab.setOnClickListener{
            NoteActivity.start(this)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }
}