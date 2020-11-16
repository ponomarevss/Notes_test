package com.example.notes.data.provider

import androidx.lifecycle.MutableLiveData
import com.example.notes.data.entity.Note
import com.example.notes.data.entity.User
import com.example.notes.data.errors.NoAuthException
import com.example.notes.data.model.NoteResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreDataProvider(val store : FirebaseFirestore, val auth: FirebaseAuth): DataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val currentUser
        get() = auth.currentUser

    private val notesReference
        get() =  currentUser?.let { store.collection(USERS_COLLECTION).document(it.uid).collection(
            NOTES_COLLECTION) } ?: throw NoAuthException()

    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }

    override fun getNotes() = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.addSnapshotListener{ snapshot, error ->
                error?.let {
                    value = NoteResult.Error(it)
                    return@addSnapshotListener
                }
                snapshot?.let { it ->
                    val notes = it.documents.map { it.toObject(Note::class.java) }
                    value = NoteResult.Success(notes)
                }
            }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun saveNote(note: Note) = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun deleteNote(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.document(id).delete()
                .addOnSuccessListener {
                    value = NoteResult.Success(null)
                }
                .addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.document(id).get()
                .addOnSuccessListener {snapshot ->
                    val note = snapshot.toObject(Note::class.java) as Note
                    value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }
}