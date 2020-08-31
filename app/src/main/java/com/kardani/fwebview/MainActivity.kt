package com.kardani.fwebview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.kardani.fwebview.databinding.ActivityMainBinding
import com.kardani.fwebview.settings.SettingsActivity
import org.koin.android.viewmodel.ext.android.viewModel


@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModel()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.setDefaultValues(this, R.xml.main_preferences, false);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        supportActionBar?.apply { hide() }


        // Observer url from ViewModel and load in WebView
        viewModel.currentUrl.observe(this, Observer { url ->
            binding.etUrl.setText(url)
            loadUrl(url)
        })

        // Observer navigate to settings
        viewModel.navigateSettings.observe(this, Observer { navigate ->

            if (!navigate) {
                return@Observer
            }

            startActivity(Intent(this, SettingsActivity::class.java))
        })

        // Observer url from ViewModel and load in WebView
        viewModel.suggestedUrls.observe(this, Observer { list ->
            val adapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, list.toList())

            binding.etUrl.threshold = 2
            binding.etUrl.setAdapter(adapter)
        })

        configWebView()

        binding.etUrl.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                binding.etUrl.text?.let {
                    viewModel.newUrl(it.toString())
                }

                return@OnKeyListener true
            }
            false
        })

    }

    private fun configWebView(){

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(viewx: WebView, urlx: String): Boolean {
                refreshConfig()
                viewModel.newUrl(urlx)
                viewx.loadUrl(urlx)
                return false
            }

        }

        binding.webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {

                viewModel.loadProgress(newProgress)

                super.onProgressChanged(view, newProgress)
            }
        }
    }

    private fun refreshConfig(){

        //cache enable
        binding.webView.settings.setAppCacheEnabled(viewModel.isCacheEnable())
        if(!viewModel.isCacheEnable()){
            binding.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }else{
            binding.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }


        //enable javascript
        binding.webView.settings.domStorageEnabled = viewModel.isJavascriptEnable()
        binding.webView.settings.javaScriptEnabled = viewModel.isJavascriptEnable()


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
//        }else{
//            CookieManager.getInstance().setAcceptCookie(true)
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val cookieManager = CookieManager.getInstance()
//            cookieManager.setAcceptCookie(true)
//            cookieManager.setAcceptThirdPartyCookies(binding.webView, true)
//        }
//
//        binding.webView.settings.setAppCachePath(applicationContext.filesDir.absolutePath + "/cache")
//        binding.webView.settings.databaseEnabled = true

    }

    private fun loadUrl(url: String){


        refreshConfig()

        binding.webView.loadUrl(url)

        binding.webView.settings.allowFileAccess = true

        when {
            Build.VERSION.SDK_INT >= 21 -> {
                binding.webView.settings.mixedContentMode = 0
                binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            }
            Build.VERSION.SDK_INT >= 19 -> {
                binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            }
            else -> {
                binding.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
        }

    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            doubleBackToExitPressedOnce = true

            val toast = Toast.makeText(
                this,
                "Double press for exit!",
                Toast.LENGTH_SHORT
            )
            val v = toast.view.findViewById<TextView>(android.R.id.message)
            v.textSize = 13f
            toast.show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

}