package com.kardani.fwebview

import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kardani.fwebview.local.UrlHistory
import com.kardani.fwebview.utils.AppPreferences
import com.kardani.fwebview.utils.LiveEvent

class MainViewModel(private val urlHistory : UrlHistory, private val appPreferences: AppPreferences) : ViewModel() {



    private var _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private var _currentUrl = MutableLiveData<String>()
    val currentUrl : LiveData<String> = _currentUrl

    private var _suggestedUrls = MutableLiveData<List<String>>()
    val suggestedUrls : LiveData<List<String>> = _suggestedUrls

    val navigateSettings = LiveEvent<Boolean>()

    init {
        _currentUrl.value = urlHistory.getLast()
        _suggestedUrls.value = urlHistory.filterUrls("")
    }

    fun newUrl(url: String){

        var newUrl = url

        if(!newUrl.startsWith("http://") && !newUrl.startsWith("https://")){
            newUrl = "https://$url"
        }

        if(!validUrl(newUrl)){
            return
        }

        urlHistory.addUrl(newUrl)

        _currentUrl.value = newUrl
    }

    fun loadProgress(progress: Int){

        if(!appPreferences.isLoadingEnable()){
            _loading.value = false
            return
        }

        _loading.value = progress < 100
    }

    fun settingsClick(){
        navigateSettings.postValue(true)
    }

    fun isCacheEnable() : Boolean{
        return appPreferences.isCacheEnable()
    }

    fun isJavascriptEnable() : Boolean{
        return appPreferences.isJavascriptEnable()
    }


    private fun validUrl(url: String) : Boolean{

        if(!URLUtil.isValidUrl(url) || !Patterns.WEB_URL.matcher(url).matches()){
            return false
        }

        return true
    }
}