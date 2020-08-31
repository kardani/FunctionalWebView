package com.kardani.fwebview.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.kardani.fwebview.MainViewModel
import com.kardani.fwebview.local.UrlHistory
import com.kardani.fwebview.utils.AppPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }

    single {
        UrlHistory(get())
    }

    single {
        AppPreferences(get())
    }

    viewModel { MainViewModel(get(), get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences{
//    return  androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
    return PreferenceManager.getDefaultSharedPreferences(androidApplication)
}