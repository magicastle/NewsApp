package com.example.newsapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.example.newsapp.adapter.MyNewsListAdapter;
import com.example.newsapp.bean.NewsCollectionsOrHistoryBean;
import com.example.newsapp.database.NewsCollectionsDao;
import com.example.newsapp.model.SingleNews;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends BaseActivity{
    private List<NewsCollectionsOrHistoryBean> collectionsList;
    private List<SingleNews> newsList;
    private NewsCollectionsDao dao = new NewsCollectionsDao();
    private RecyclerView recyclerView;
    private MyNewsListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initView();
    }

    public void initData(){
        collectionsList = dao.query();
        System.out.println("collections List items num : " + collectionsList.size());
        newsList = new ArrayList<>();
        for(int i =  collectionsList.size() - 1; i >= 0; i --){
            newsList.add(new SingleNews(
                    collectionsList.get(i).getImage(),
                    collectionsList.get(i).getPublishTime(),
                    collectionsList.get(i).getPublisher(),
                    collectionsList.get(i).getTitle(),
                    collectionsList.get(i).getContent(),
                    null,
                    null,
                    collectionsList.get(i).getNewsID(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
            System.out.println(collectionsList.get(i).getImage());
        }
    }
    public void initView(){
        adapter = new MyNewsListAdapter(newsList, this);
        recyclerView = findViewById(R.id.collections_rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft()
        ;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
