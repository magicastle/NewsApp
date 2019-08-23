package com.example.newsapp;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.bean.NewsChannelBean;
import com.example.newsapp.database.NewsChannelDao;
import com.example.newsapp.helper.ItemDragHelperCallback;
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

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
}

