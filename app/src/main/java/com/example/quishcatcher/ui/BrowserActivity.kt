package com.example.quishcatcher.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.quishcatcher.api.Constants
import com.example.quishcatcher.databinding.ActivityBrowserBinding

class BrowserActivity : AppCompatActivity() {

    companion object {
        fun openBrowserActivity(activity: Context) {
            val intent = Intent(activity, BrowserActivity::class.java)
            activity.startActivity(intent)
        }
    }

    lateinit var binding: ActivityBrowserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivClose.setOnClickListener {
            finish()
        }

        openWebsite(Constants.browsableUrl)

    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebsite(url: String) {
        try {
            val mWebSettings: WebSettings = binding.webView.settings
            binding.webView.webViewClient = WebViewClient()
            mWebSettings.javaScriptEnabled = true
            mWebSettings.domStorageEnabled = true
            mWebSettings.allowFileAccess = true
            mWebSettings.allowFileAccess = true
            mWebSettings.allowContentAccess = true
            binding.webView.loadUrl(url)
            binding.swipeRefresh.setOnRefreshListener { binding.webView.loadUrl(binding.webView.url.toString()) }
            binding.webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = progress * 100
                    if (progress == 100) {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        binding.tvTitle.text = binding.webView.title
                    }
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "Unknown Error", Toast.LENGTH_LONG).show()
        }
    }
}