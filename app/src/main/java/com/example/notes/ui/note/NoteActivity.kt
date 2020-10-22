package com.example.notes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notes.R
import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private const val EXTRA_NOTE = "note"

        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null)
                = Intent(context, NoteActivity::class.java)
            .apply {
            putExtra(EXTRA_NOTE, noteId)
            context.startActivity(this)
        }
    }

    override val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }
    override val layoutResource = R.layout.activity_note
    private var note: Note? = null

    private val textChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) = saveNote()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        noteId?.let {
            viewModel.loadNote(it)
            viewModel.removeObserver()
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        initView()
    }

    private fun initView() {
        title_et.removeTextChangedListener(textChangeListener)
        body_et.removeTextChangedListener(textChangeListener)

        note?.let {
            title_et.setText(it.title)
            body_et.setText(it.body)

            val color = when(it.color) {
                Note.Color.WHITE -> R.color.color_white
                Note.Color.YELLOW -> R.color.color_yellow
                Note.Color.GREEN -> R.color.color_green
                Note.Color.BLUE -> R.color.color_blue
                Note.Color.RED -> R.color.color_red
                Note.Color.VIOLET -> R.color.color_violet
            }
            toolbar.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
        }
        title_et.addTextChangedListener(textChangeListener)
        body_et.addTextChangedListener(textChangeListener)
    }

    private fun saveNote() {
        if (title_et.text == null || title_et.text!!.length < 3) return
        if (body_et.text == null || body_et.text!!.length < 3) return

        note = note?.copy(
            title = title_et.text.toString(),
            body = body_et.text.toString(),
            lastChanged = Date()
        ) ?: Note(UUID.randomUUID().toString(), title_et.text.toString(), body_et.text.toString())

        note?.let { viewModel.save(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.new_note_title)

        initView()
    }
}