package com.kardani.fwebview.utils

import android.content.SharedPreferences

class AppPreferences(private val preferences: SharedPreferences) {

    fun isCacheEnable() : Boolean{
        return preferences.getBoolean("cache", false)
    }

    fun isJavascriptEnable() : Boolean{
        return preferences.getBoolean("javascript", false)
    }

    fun isLoadingEnable() : Boolean{
        return preferences.getBoolean("loading", false)
    }

}