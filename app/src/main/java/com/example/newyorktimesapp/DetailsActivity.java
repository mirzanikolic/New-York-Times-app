package com.example.newyorktimesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

        progressBar = findViewById(R.id.loading_bar2);
        webView = findViewById(R.id.news_web_view);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            webUrl = extras.getString("newsUrl");
            showDetails();
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_LONG);
        }

    }

    private static class CallBack extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }

    //Show the webView component filled with data.
    @SuppressLint("SetJavaScriptEnabled")
    public void showDetails(){
        progressBar.setVisibility(View.INVISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new CallBack());
        webView.loadUrl(webUrl);
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