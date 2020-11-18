package com.example.notes.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.notes.data.NotesRepository
import com.example.notes.data.entity.Note
import com.example.notes.data.model.NoteResult
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest{
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val noteLiveData = MutableLiveData<NoteResult>()
    private val deleteLiveData = MutableLiveData<NoteResult>()
    private val saveLiveData = MutableLiveData<NoteResult>()

    private val testNote = Note("1", "title", "body")

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { mockRepository.getNoteById(testNote.id) } returns noteLiveData
        every { mockRepository.deleteNote(testNote.id) } returns deleteLiveData
        every { mockRepository.saveNote(testNote) } returns saveLiveData
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `loadNote should return NoteViewState Data`(){
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(testNote, false)
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testData)
        assertEquals(result, testData)
    }

    @Test
    fun `loadNote should return error`(){
        var result: Throwable? = null
        val testError = Throwable("testError")
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Error(testError)
        assertEquals(result, testError)
    }

    @Test
    fun `deleteNote should return NoteViewState with isDeteled`(){
        var result: NoteViewState.Data? = null
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)

        viewModel.deleteNote()
        deleteLiveData.value = NoteResult.Success(null)
        assertEquals(result?.isDeleted, true)
    }

    @Test
    fun `deleteNote should return error`(){
        var result: Throwable? = null
        val testError = Throwable("testError")
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)

        viewModel.deleteNote()
        deleteLiveData.value = NoteResult.Error(testError)
        assertEquals(result, testError)
    }

    @Test
    fun `should save changes`(){
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)

        viewModel.onCleared()
        verify(exactly = 1) { mockRepository.saveNote(testNote) }
    }

}