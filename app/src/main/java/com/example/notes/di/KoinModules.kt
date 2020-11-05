package com.example.notes.di

import com.example.notes.data.NotesRepository
import com.example.notes.data.provider.DataProvider
import com.example.notes.data.provider.FirestoreDataProvider
import com.example.notes.ui.main.MainViewModel
import com.example.notes.ui.note.NoteViewModel
import com.example.notes.ui.splash.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirestoreDataProvider(get(), get()) } bind DataProvider::class
    single { NotesRepository(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}