package com.example.newsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.*;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.*;
import com.bumptech.glide.request.target.Target;
import com.example.newsapp.model.SingleNews;
import com.google.android.material.appbar.AppBarLayout;


public class NewsDetailActivity extends AppCompatActivity {

    private SingleNews newsDetail;
    private TextView contentTextView;
    private TextView titleTextView;

    private ImageView imageView;
    //private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
//        toolbar = findViewById(R.id.toolb);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // find component
        newsDetail = (SingleNews) getIntent().getSerializableExtra("newsDetail");

        titleTextView = findViewById(R.id.new_detail_title_tv);
        titleTextView.setText(newsDetail.getTitle()+"\n");
        contentTextView= findViewById(R.id.new_detail_content_tv);
        contentTextView.setText(newsDetail.getContent());
        imageView=findViewById(R.id.news_image);
        String urls[]=newsDetail.getImage();//images lists
        if(urls.length>0)
        {
            System.out.println(urls[0]);

            RequestListener mRequestListener = new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    Log.d("NewsDetailActivity", "onException: " + e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    return false;
                }
                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    Log.e("NewsDetailActivity",  "model:"+model+" isFirstResource: "+isFirstResource);
                    return false;
                }
            };
            RequestOptions options = new RequestOptions()
                    //.centerCrop()
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .error(new ColorDrawable(Color.RED))
                    .priority(Priority.HIGH);

            Glide.with(this)
                    .load(urls[0])
                    .apply(options)
                    .listener(mRequestListener)
                    .into(imageView);
        }
        else
            imageView.setVisibility(View.GONE);

        Toast.makeText(this, "image"+urls.length , Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.hide,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.share:
                Toast.makeText(this,"share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.mark:
                Toast.makeText(this,"mark",Toast.LENGTH_SHORT).show();
                break;
            case R.id.download:
                Toast.makeText(this,"download",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

}
