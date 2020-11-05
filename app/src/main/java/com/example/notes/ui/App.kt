package com.example.notes.ui

import android.app.Application
import com.example.notes.di.appModule
import com.example.notes.di.mainModule
import com.example.notes.di.noteModule
import com.example.notes.di.splashModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, mainModule, noteModule, splashModule))
    }
}