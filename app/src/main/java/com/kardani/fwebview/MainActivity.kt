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
import com.kardani.fwebview.databinding.ActivityMainBinding
import com.kardani.fwebview.local.UrlHistory
import org.koin.android.ext.android.inject


@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    val urlHistory : UrlHistory by inject()

    lateinit var binding: ActivityMainBinding

    var loadingTimes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //changing statusbar color and navigationbar color
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.app_base_color)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.app_base_color)
        }

        supportActionBar!!.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)


        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(viewx: WebView, urlx: String): Boolean {
                viewx.loadUrl(urlx)
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingTimes = 0
                binding.lytProgress.visibility = View.GONE
            }

        }

        binding.webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                Log.d("Progress", "onProgressChanged: $newProgress")
                if(loadingTimes > 1 && newProgress == 100){
                    binding.lytProgress.visibility = View.GONE
                    loadingTimes = 0
                }else if(loadingTimes < 1 && newProgress == 100){
                    loadingTimes++
                }else{
                    binding.lytProgress.visibility = View.VISIBLE
                }

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



                return@OnKeyListener true
            }
            false
        })

        loadUrl("")

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

            // toast baraye khorooj az app
            val toast = Toast.makeText(
                this,
                "برای خروج دکمه بازگشت را دوباره فشار دهید",
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