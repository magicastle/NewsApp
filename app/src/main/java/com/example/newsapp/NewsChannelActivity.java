package com.example.newsapp;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.adapter.MyNewsChannelAdapter;
import com.example.newsapp.bean.NewsChannelBean;
import com.example.newsapp.database.NewsChannelDao;
import com.example.newsapp.helper.ItemDragHelperCallback;
import com.example.newsapp.util.Constant;

import java.util.List;

public class NewsChannelActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MyNewsChannelAdapter myNewsChannelAdapter;
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
        // 根据不同类型的viewType进行不同的布置排列, header一排1个，category item 一排4个
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = myNewsChannelAdapter.getItemViewType(position);
                return viewType == MyNewsChannelAdapter.TYPE_ENABLE || viewType == MyNewsChannelAdapter.TYPE_DISABLE ? 1 : 4;
            }
        });

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        myNewsChannelAdapter = new MyNewsChannelAdapter(this, itemTouchHelper, enableItems, disableItems);
        recyclerView.setAdapter(myNewsChannelAdapter);
    }
}

