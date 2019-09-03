package com.example.newsapp.bean;

import android.widget.ImageView;

import com.stx.xhb.androidx.entity.SimpleBannerInfo;

public class ImageUrlBean extends SimpleBannerInfo {
    String imageUrl;

    public ImageUrlBean(String image){
        this.imageUrl = image;
    }
    @Override
    public Object getXBannerUrl() {
        return imageUrl;
    }
}
