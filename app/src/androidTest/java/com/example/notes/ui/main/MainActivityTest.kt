package com.example.notes.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.example.notes.data.entity.Note
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

class MainActivityTest{

    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val viewModel = mockk<MainViewModel>(relaxed = true)
    private val viewStateLiveData = MutableLiveData<MainViewState>()

    private val testNotes = listOf(
        Note("1", "title1", "body1"),
        Note("2", "title2", "body2"),
        Note("3", "title3", "body3")
    )

    @Before
    fun setup(){
        loadKoinModules(
            listOf(
                module {
                    viewModel {viewModel}
                }
            )
        )
        every { viewModel.getViewStateLiveData() } returns viewStateLiveData
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed(){
        //TODO
    }

}