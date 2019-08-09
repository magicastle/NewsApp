package com.example.newsapp.network;

import com.example.newsapp.model.NewsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("queryNewsList")
    Call<NewsData> getNewsSearch(
            @Query("size") String size,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("words") String words,
            // categories的参数有：娱乐、军事、教育、文化、健康、财经、体育、汽车、科技、社会
            @Query("categories") String categories
    );

    @GET("queryNewsList")
    Call<NewsData> getNews();
}


