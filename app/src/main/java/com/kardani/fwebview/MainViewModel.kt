package com.kardani.fwebview

import android.util.Log
import android.util.Patterns
import android.view.View
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kardani.fwebview.local.UrlHistory

class MainViewModel(private val urlHistory : UrlHistory) : ViewModel() {

    private var _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private var _currentUrl = MutableLiveData<String>()
    val currentUrl : LiveData<String> = _currentUrl

    private var _suggestedUrls = MutableLiveData<Set<String>>()
    val suggestedUrls : LiveData<Set<String>> = _suggestedUrls

    init {
        _currentUrl.value = urlHistory.getLast()
    }

    fun newUrl(url: String){

        if(!validUrl(url)){
            return
        }

        urlHistory.addUrl(url)

        _currentUrl.value = url
    }

    fun loadProgress(progress: Int){
//        Log.d("MainViewModel", "loadProgress: $progress")
        _loading.value = progress < 100
    }

    private fun validUrl(url: String) : Boolean{

        if(!URLUtil.isValidUrl(url) || !Patterns.WEB_URL.matcher(url).matches()){
            return false
        }

        return true
    }
}