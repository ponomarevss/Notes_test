package com.example.notes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.data.entity.Note
import kotlinx.android.synthetic.main.item.view.*

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView){
            title_tv.text = note.title
            body_tv.text = note.body

            val color = when(note.color) {
                Note.Color.WHITE -> R.color.color_white
                Note.Color.YELLOW -> R.color.color_yellow
                Note.Color.GREEN -> R.color.color_green
                Note.Color.BLUE -> R.color.color_blue
                Note.Color.RED -> R.color.color_red
                Note.Color.VIOLET -> R.color.color_violet
            }
            (itemView as CardView).setBackgroundColor(ResourcesCompat.getColor(resources, color, null))

            itemView.setOnClickListener {
                onClickListener?.invoke(note)
            }
        }
    }
}