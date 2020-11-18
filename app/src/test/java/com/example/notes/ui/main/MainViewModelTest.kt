package com.example.notes.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.notes.data.NotesRepository
import com.example.notes.data.entity.Note
import com.example.notes.data.model.NoteResult
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`(){
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return Notes`(){
        var result: List<Note>? = null
        val testData = listOf<Note>(Note("1"), Note("2"))
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(result, testData)
    }

    @Test
    fun `should return error`(){
        var result: Throwable? = null
        val testError = Throwable("testError")
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }
        notesLiveData.value = NoteResult.Error(testError)
        assertEquals(result, testError)
    }

    @Test
    fun `should remove observer`(){
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }
}