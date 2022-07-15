package com.example.newyorktimesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import javax.security.auth.callback.Callback;

public class DetailsActivity extends AppCompatActivity {
    WebView webView;
    String webUrl;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        webView = findViewById(R.id.news_web_view);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            webUrl = extras.getString("newsUrl");
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setWebViewClient(new CallBack());
            webView.loadUrl(webUrl);

        }

    }

    private static class CallBack extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
}