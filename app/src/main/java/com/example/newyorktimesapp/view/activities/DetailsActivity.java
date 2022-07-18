package com.example.newyorktimesapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newyorktimesapp.R;
import com.example.newyorktimesapp.databinding.ActivityDetailsBinding;

import java.time.Duration;

import javax.security.auth.callback.Callback;

public class DetailsActivity extends AppCompatActivity {
    WebView webView;
    String webUrl;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        InitUI();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            webUrl = extras.getString("newsUrl");
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(webUrl);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

    }

    private void InitUI(){
        progressBar = findViewById(R.id.loading_bar2);
        webView = findViewById(R.id.news_web_view);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        boolean loadingFinished = true;
        boolean redirect = false;

        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, WebResourceRequest request) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            webView.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(
                WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
            loadingFinished = false;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                progressBar.setVisibility(View.GONE);
                loadingFinished = true;
            } else {
                redirect = false;
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }//close activity when click back button
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    //Back button goes back in the web view and not in the app.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}