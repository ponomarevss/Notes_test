package com.example.notes.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notes.R
import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseActivity
import com.example.notes.ui.note.NoteActivity
import com.example.notes.ui.splash.SplashActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override val layoutResource = R.layout.activity_main
    lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        notes_rv.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRVAdapter {
            NoteActivity.start(this, it.id)
        }

        notes_rv.adapter = adapter

        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout -> showLogoutDialog().let { true }
            else -> false
        }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.logout_ok) { dialog, which -> logout() }
            .setNegativeButton(R.string.logout_cancel) { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                SplashActivity.start(this)
                finish()
            }
    }
}