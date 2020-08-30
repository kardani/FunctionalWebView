package com.kardani.fwebview

import android.util.Log
import android.util.Patterns
import android.view.View
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kardani.fwebview.local.UrlHistory
import com.kardani.fwebview.utils.LiveEvent

class MainViewModel(private val urlHistory : UrlHistory) : ViewModel() {



    private var _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private var _currentUrl = MutableLiveData<String>()
    val currentUrl : LiveData<String> = _currentUrl

    private var _suggestedUrls = MutableLiveData<Set<String>>()
    val suggestedUrls : LiveData<Set<String>> = _suggestedUrls

    val navigateSettings = LiveEvent<Boolean>()

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
        _loading.value = progress < 100
    }

    fun settingsClick(){
        navigateSettings.postValue(true)
    }

    private fun validUrl(url: String) : Boolean{

        if(!URLUtil.isValidUrl(url) || !Patterns.WEB_URL.matcher(url).matches()){
            return false
        }

        return true
    }
}