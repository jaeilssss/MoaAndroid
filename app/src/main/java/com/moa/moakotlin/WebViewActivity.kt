 package com.moa.moakotlin

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


 class WebViewActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var back : ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.webView)
        webView.getSettings().setJavaScriptEnabled(true);
        MyApp.isForeground = true
        val settings = webView.settings
        settings.domStorageEnabled = true

        
        back  = findViewById(R.id.WebViewBack)
        var url  = intent.getStringExtra("url")
        if (url != null) {
            init(url)
        }
        back.setOnClickListener {
            finish()
        }
    }

     private fun init(url: String){
         webView.webViewClient = object : WebViewClient(){
             override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                 super.onPageStarted(view, url, favicon)

             }

             override fun onPageFinished(view: WebView?, url: String?) {
                 super.onPageFinished(view, url)
             }

             override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                 if (url != null) {
                     view?.loadUrl(url)
                 }
                 return true
             }
         }

         var ws = webView.settings

         ws.javaScriptEnabled =true

         webView.loadUrl(url)
     }
}