package com.example.newsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.bumptech.glide.*;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.*;
import com.bumptech.glide.request.target.Target;
import com.example.newsapp.database.NewsCollectionsDao;
import com.example.newsapp.model.SingleNews;


import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


public class NewsDetailActivity extends AppCompatActivity {

    private SingleNews news;
    private TextView contentTextView;
    private TextView titleTextView;

    private ImageView imageView;
    private Switch switchbutton;
    private ImageView share;
    private ImageView collection;
    private View.OnClickListener viewClickListener;
    private NewsCollectionsDao collectionsDao = new NewsCollectionsDao();
    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // find component
        news = (SingleNews) getIntent().getSerializableExtra("news");

        if(news != null){

            initView();
        }
    }

    public void initView(){
        titleTextView = findViewById(R.id.new_detail_title_tv);
        titleTextView.setText(news.getTitle());
        contentTextView= findViewById(R.id.new_detail_content_tv);
        contentTextView.setText(news.getContent());

        share=new ImageView(this);
        share.setId(R.id.myShare);
        //share.setPadding(60,0,0,0);
        share.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_white_24dp));
        getSupportActionBar().setCustomView(share);


        collection=new ImageView(this);
        collection.setId(R.id.myCollection);
        collection.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
        //getSupportActionBar().setCustomView(collection);

        viewClickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId())
                {
                    case R.id.myShare:
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        //shareIntent.setData(Uri.parse("myapp://dosomething"));
                        shareIntent.putExtra(Intent.EXTRA_TEXT, news.getAbstract() + "\n" + news.getUrl());
                        shareIntent.setType("text/plain");
                        startActivity(Intent.createChooser(shareIntent, "send to..."));
                        break;
                }
            }
        };

        share.setOnClickListener(viewClickListener);
        collection.setOnClickListener(viewClickListener);

        initImageView();
        initVideoView();

        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft()
        ;

    }

    public void initVideoView(){
        String videoUrl = news.getVideo();
        videoPlayer = findViewById(R.id.video_player);
        if(videoUrl.equals("")){
            videoPlayer.setVisibility(View.INVISIBLE);
        }
        else{
            //String videoUrl = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
            videoPlayer.setUp(videoUrl, true, "测试视频");
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.ic_favorite_white_24dp);
            videoPlayer.setThumbImageView(imageView);
            //增加title
            videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
            //设置返回键
            videoPlayer.getBackButton().setVisibility(View.VISIBLE);
            //设置旋转
            orientationUtils = new OrientationUtils(this, videoPlayer);
            //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orientationUtils.resolveByClick();
                }
            });
            //是否可以滑动调整
            videoPlayer.setIsTouchWiget(true);
            //设置返回按键功能
            videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            videoPlayer.startPlayLogic();

        }
    }

    public void initImageView(){
        imageView=findViewById(R.id.news_image);
        String urls[]= news.getImage();//images lists
        if(urls.length>0)
        {
            RequestListener mRequestListener = new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    //Log.d("NewsDetailActivity", "onException: " + e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    return false;
                }
                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    // Log.e("NewsDetailActivity",  "model:"+model+" isFirstResource: "+isFirstResource);

                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                    float scale = (float) vw /(float) (((Drawable) resource).getIntrinsicWidth());
                    int vh = Math.round(((Drawable) resource).getIntrinsicHeight() * scale);
                    params.height=vh + imageView.getPaddingTop()+imageView.getPaddingBottom();
                    imageView.setLayoutParams(params);
                    return false;
                }
            };
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .error(new ColorDrawable(Color.RED))
                    .priority(Priority.HIGH);


            Glide.with(this)
                    .load(urls[0])
                    .apply(options)
                    .listener(mRequestListener)
                    .into(imageView);
        }
        else{
            imageView.setVisibility(View.GONE);
        }
        Toast.makeText(this, "image"+urls.length , Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.hide,menu);

        switchbutton=(Switch) menu.findItem(R.id.switchbutton).getActionView().findViewById(R.id.switchForActionBar);

        if(news != null){
            if(collectionsDao.contain(news.getNewsID())){
                switchbutton.setChecked(true);
            }
        }

        if(switchbutton != null){
            switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {
                        collectionsDao.add(
                                news.getNewsID(),
                                news.getImageString(),
                                news.getPublishTime(),
                                news.getPublisher(),
                                news.getTitle(),
                                news.getContent(),
                                news.getKeywords()
                        );
                    }
                    else
                    {
                        collectionsDao.delete(news.getNewsID());
                    }
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
//            case R.id.share:
//
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                //shareIntent.setData(Uri.parse("myapp://dosomething"));
//                shareIntent.putExtra(Intent.EXTRA_TEXT, news.getAbstract()+"\n"+ news.getUrl());
//                shareIntent.setType("text/plain");
//                startActivity(Intent.createChooser(shareIntent, "send to..."));
//
//                break;


            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
};
