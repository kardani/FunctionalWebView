package com.kardani.fwebview.di

import android.app.Application
import android.content.SharedPreferences
import com.kardani.fwebview.local.UrlHistory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single{
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }

    single {
        getSharedPrefs(androidApplication())
    }

    single {
        UrlHistory(getSharedPrefs(androidApplication()))
    }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences{
    return  androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
}