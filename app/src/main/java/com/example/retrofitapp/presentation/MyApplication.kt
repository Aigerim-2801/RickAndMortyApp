package com.example.retrofitapp.presentation

import android.app.Application
import com.example.retrofitapp.presentation.di.appModule
import com.example.retrofitapp.presentation.di.databaseModule
import com.example.retrofitapp.presentation.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(appModule, databaseModule, networkModule))
        }
    }
}