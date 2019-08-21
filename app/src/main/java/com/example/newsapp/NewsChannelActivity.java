package com.example.newsapp;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.bean.NewsChannelBean;
import com.example.newsapp.database.NewsChannelDao;
import com.example.newsapp.util.Constant;

import java.util.List;

public class NewsChannelActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private NewsChannelDao dao = new NewsChannelDao();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initView();
        initData();
    }

    public void initView(){
        //initToolBar((Toolbar)findViewById(R.id.toolbar), true, getString(R.string.channel_manager));
        recyclerView = findViewById(R.id.news_channel_rcv);
    }

    public void initData(){
        final List<NewsChannelBean> enableItems = dao.query(Constant.NEWS_CHANNEL_ENABLE);
        final List<NewsChannelBean> disableItems = dao.query(Constant.NEWS_CHANNEL_DISABLE);
        for(int i = 0;i < enableItems.size();i ++){
            System.out.println(enableItems.get(i).getChannelName());
        }
        for(int i = 0;i < disableItems.size();i ++){
            System.out.println(disableItems.get(i).getChannelName());
        }
        if(enableItems.size() == 0){
            System.out.println("init failure");
        }
    }
}

