package com.example.newsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsapp.model.SingleNews;
import com.google.android.material.appbar.AppBarLayout;

public class NewsDetailActivity extends AppCompatActivity {

    private TextView contentTextView;
    private TextView titleTextView;
    private SingleNews newsDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        // find component
        newsDetail = (SingleNews) getIntent().getSerializableExtra("newsDetail");
        titleTextView = findViewById(R.id.new_detail_title_tv);
        titleTextView.setText(newsDetail.getTitle() + "\n");
        contentTextView= findViewById(R.id.new_detail_content_tv);
        contentTextView.setText(newsDetail.getContent());
    }
}
