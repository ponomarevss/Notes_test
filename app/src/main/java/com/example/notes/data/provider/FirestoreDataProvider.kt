package com.example.notes.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes.data.entity.Note
import com.example.notes.data.model.NoteResult
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreDataProvider: DataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)

    override fun getNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener{snapshot, error ->
            error?.let {
                result.value = NoteResult.Error(it)
                return@addSnapshotListener
            }
            snapshot?.let { it ->
                val notes = it.documents.map { it.toObject(Note::class.java) }
                result.value = NoteResult.Success(notes)
            }
        }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note)
            .addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }
            .addOnFailureListener {
                result.value = NoteResult.Error(it)
            }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
            .addOnSuccessListener {snapshot ->
                val note = snapshot.toObject(Note::class.java) as Note
                result.value = NoteResult.Success(note)
            }
            .addOnFailureListener {
                result.value = NoteResult.Error(it)
            }
        return result
    }
}