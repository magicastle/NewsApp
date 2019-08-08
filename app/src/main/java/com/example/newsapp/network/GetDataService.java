package com.example.newsapp.network;

import com.example.newsapp.model.NewsData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("queryNewsList")
    Call<NewsData> getAllNews();
}
