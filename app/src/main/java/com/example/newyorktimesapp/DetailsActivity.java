package com.example.newyorktimesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    TextView textView;
    String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        textView = findViewById(R.id.text_view);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            webUrl = extras.getString("newsUrl");
            textView.setText(webUrl);
        }

    }
}