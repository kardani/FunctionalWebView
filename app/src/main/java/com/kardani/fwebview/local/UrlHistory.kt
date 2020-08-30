package com.kardani.fwebview.local

import android.content.SharedPreferences

class UrlHistory(private val preferences: SharedPreferences) {

    private val KEY = "URL_HISTORY"

    fun addUrl(url: String){
        preferences.edit().putStringSet(KEY, getUrls().plus(url)).apply()
    }

    private fun getUrls() : Set<String>{
        return preferences.getStringSet(KEY, setOf()) ?: setOf()
    }

    fun filterUrls(expr: String? = null) : List<String>{

        val urls = getUrls()

        if(expr.isNullOrEmpty()){
            return urls.toList()
        }

        return urls.filter { it.contains(expr) }
    }

}