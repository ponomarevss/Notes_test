package com.example.notes.ui.note

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import com.example.notes.R
import com.example.notes.common.getColorInt
import com.example.notes.data.entity.Note
import com.example.notes.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    companion object {
        private const val EXTRA_NOTE = "note"

        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) =
            Intent(context, NoteActivity::class.java)
                .apply {
                    putExtra(EXTRA_NOTE, noteId)
                    context.startActivity(this)
                }
    }

    override val viewModel: NoteViewModel by viewModel()
    override val layoutResource = R.layout.activity_note
    private var note: Note? = null
    var color = Note.Color.WHITE

    private val textChangeListener = object : TextWatcher {
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

            val color = when (it.color) {
                Note.Color.WHITE -> R.color.color_white
                Note.Color.YELLOW -> R.color.color_yellow
                Note.Color.GREEN -> R.color.color_green
                Note.Color.BLUE -> R.color.color_blue
                Note.Color.RED -> R.color.color_red
                Note.Color.VIOLET -> R.color.color_violet
            }
            toolbar.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
        }

        colorPicker.onColorClickListener = {
            toolbar.setBackgroundColor(it.getColorInt(this))
            color = it
            saveNote()
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
            lastChanged = Date(),
            color = color
        ) ?: Note(UUID.randomUUID().toString(), title_et.text.toString(), body_et.text.toString(), color = color)

        note?.let { viewModel.save(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu) = menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.palette -> togglePicker().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()

        this.note = data.note
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.new_note_title)

        initView()
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setTitle(R.string.note_delete_title)
            .setMessage(R.string.note_delete_message)
            .setPositiveButton(R.string.note_delete_ok) { dialog, which ->
                viewModel.deleteNote()
            }
            .setNegativeButton(R.string.note_delete_cancel) { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun togglePicker() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }
}
