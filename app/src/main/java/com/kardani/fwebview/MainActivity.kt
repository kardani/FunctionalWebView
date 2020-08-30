package com.kardani.fwebview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kardani.fwebview.databinding.ActivityMainBinding
import com.kardani.fwebview.settings.SettingsActivity
import org.koin.android.viewmodel.ext.android.viewModel


@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModel()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel



        supportActionBar?.apply { hide() }

        // Observer url from ViewModel and load in WebView
        viewModel.currentUrl.observe(this, Observer{ url ->
            loadUrl(url)
        })

        viewModel.navigateSettings.observe(this, Observer{ navigate ->

            if(!navigate){
                return@Observer
            }

            startActivity(Intent(this, SettingsActivity::class.java))
        })

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(viewx: WebView, urlx: String): Boolean {
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

        binding.etUrl.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val url = binding.etUrl.text?.toString()

                if(url.isNullOrEmpty()){
                    return@OnKeyListener false
                }

                if(!URLUtil.isValidUrl(url) || !Patterns.WEB_URL.matcher(url).matches()){
                    return@OnKeyListener false
                }

                viewModel.newUrl(url)

                return@OnKeyListener true
            }
            false
        })

        setContentView(binding.root)
    }

    private fun loadUrl(url: String){

        //cache enable
        binding.webView.settings.setAppCacheEnabled(false);
        binding.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE;

        //enable javascript
        binding.webView.settings.domStorageEnabled = true;
        binding.webView.settings.javaScriptEnabled = true


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

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
//            overridePendingTransition(0, R.anim.anim_activity_fade_out)
                return
            }
            doubleBackToExitPressedOnce = true

            val toast = Toast.makeText(
                this,
                "Double press for exit!",
                Toast.LENGTH_SHORT
            )
            val v = toast.view.findViewById<TextView>(android.R.id.message)
//        v.setTypeface(font)
            v.textSize = 13f
            toast.show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

}