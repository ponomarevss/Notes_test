package com.example.notes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.common.getColorInt
import com.example.notes.data.entity.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*

class NotesRVAdapter(val onClickListener: ((Note) -> Unit)? = null)
    : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item, parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    override fun getItemCount() = notes.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(note: Note) {
            title_tv.text = note.title
            body_tv.text = note.body

            (itemView as CardView).setBackgroundColor(note.color.getColorInt(containerView.context))

            itemView.setOnClickListener {
                onClickListener?.invoke(note)
            }
        }
    }
}