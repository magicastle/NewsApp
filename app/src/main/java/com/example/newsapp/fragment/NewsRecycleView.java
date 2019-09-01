package com.example.newsapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.newsapp.R;
import com.example.newsapp.adapter.MyNewsListAdapter;
import com.example.newsapp.database.NewsCollectionsDao;
import com.example.newsapp.database.NewsHistoryDao;
import com.example.newsapp.model.NewsData;
import com.example.newsapp.model.SingleNews;
import com.example.newsapp.network.GetDataService;
import com.example.newsapp.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRecycleView extends Fragment {
    private View view;
    // search options
    private Integer pageSize = 10;
    private String startDate = "2019-07-01 13:12:45";
    private String endDate   = "2021-08-03 18:42:20";
    private String words = "";
    private String queryWords = "";
    private String category = "";
    private String queryCategory = "";
    private Integer pageNum = 1;
    // judge whether be the end
    private Boolean onEarth = false;
//    private Boolean requestSuccessful = false;

    private ProgressDialog progressDialog;
    private NewsData newsData;
    private List<SingleNews> newsList;
    private HashSet<String> newsIDSet;
    private List<String> keywordsList;
    private int keywordsIndex = 0;
    private int recursionDepth = 0;
    private RecyclerView recyclerView;
    private MyNewsListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout errorLayout;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;
    private EasyRefreshLayout easyRefreshLayout;
    private NewsHistoryDao historyDao = new NewsHistoryDao();
    private NewsCollectionsDao collectionsDao = new NewsCollectionsDao();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news_list, container, false);

        // init search options
        Bundle bundle = getArguments();
        words = bundle.getString("words");
        category = bundle.getString("category");
        pageNum = 1;

        initView();
        request();
        return view;
    }

    public void initView(){
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        newsList = new ArrayList<>();
        newsIDSet = new HashSet<>();
        mAdapter = new MyNewsListAdapter(newsList, getActivity());
        recyclerView.setAdapter(mAdapter);

        initEasyRreshLayout();
        initErrorLayout();
    }
    public void initEasyRreshLayout(){
        easyRefreshLayout = view.findViewById(R.id.easy_refresh_layout);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                if(onEarth == false){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageNum++;
                            request();
                            easyRefreshLayout.loadMoreComplete();
                        }
                    }, 1000);
                }
                else{
                    Toast.makeText(getActivity(), "On Earth !!", Toast.LENGTH_SHORT).show();
                    easyRefreshLayout.loadMoreComplete();
                }
            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newsList.clear();
                        pageNum = 1;
                        request();
                        easyRefreshLayout.refreshComplete();
                        Toast.makeText(getActivity(), "Refreshed!", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
    }
    public void initErrorLayout(){
        errorLayout = view.findViewById(R.id.errorLayout);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        errorLayout.setVisibility(View.GONE);
    }
    public void connect(){
        // 根据category类别初始化 queryWords 和 queryCategory
        if(this.category.equals("推荐")){
            if(keywordsIndex < keywordsList.size()) {
                this.queryWords = keywordsList.get(keywordsIndex);
            }
            else{
                this.queryWords = "";
            }
            this.queryCategory = "";
        }
        else{
            this.queryWords = this.words;
            this.queryCategory = this.category;
        }

        // 网络请求
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NewsData> call;
        call = service.getNewsSearch(
                this.pageSize.toString(),
                this.startDate,
                this.endDate,
                this.queryWords,
                this.queryCategory,
                this.pageNum.toString()
        );
        call.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                if(response.isSuccessful()){
                    List<SingleNews> requestList = response.body().getData();

                    // 没有内容，说明已经达到底部，更新onEarth标志
                    if(requestList.size() == 0){
                        Toast.makeText(getActivity(), "On Earth !!", Toast.LENGTH_SHORT).show();
                        System.out.println("on earth" + queryWords + " " + queryCategory);
                        onEarth = true;
                        return;
                    }

                    // 成功返回内容，更新 newsList
                   if(category.equals("推荐")){
                        /*
                            注意新闻填充逻辑：因为加入了要填满pageNum * pageSize 的限制以及历史信息不填充的限制
                            所以要避免出现为达到要求发生死循环
                         */
                        System.out.println("Yeah ,tuijian " + queryWords + " " + queryCategory);
                        // 避免推荐列表重复，添加newsIDSet control
//                        int tmp = 0;
                        for (SingleNews news : requestList){
                            /*
                                添加条件：
                                1. 当前列表不重复
                                2. 未点击看过
                                3. 新闻数量未满
                             */
                            String id = news.getNewsID();
                            if(!newsIDSet.contains(id) && !historyDao.contain(id) && newsList.size() < pageNum *pageSize){
                                newsIDSet.add(id);
                                newsList.add(news);
//                                if(!queryWords.equals(""))
//                                    tmp ++;
//                                if(tmp > 5)
//                                    break;
                            }
                        }
                        keywordsIndex ++;
                        if(keywordsIndex < keywordsList.size() + 2){
                            recursionDepth ++;
                            connect();
                        }

//                        if(newsList.size() < pageNum * pageSize){
//                            keywordsIndex ++;
//                            recursionDepth ++;
//                            connect();
//                        }
                    }
                    else{
                        newsList.addAll(requestList);
                    }

                    if(recursionDepth == 0){
                        System.out.println("notifyDataseteChanged");
                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        recursionDepth --;
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                Toast.makeText(getActivity(), "Load error... maybe retry...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void request(){
        if(this.category.equals("推荐")){
            /*
                获取搜索关键词策略：
                历史选 3 条，收藏 1 条，每条各选最多 2 个keyWord,形成总的KeyWord列表
                对于Keyword例表中的每个词，进行一次查询操作,向List中添加5条新闻
                添加一直到满page * pagesize为止
                有可能上面策略不能满足填满 pageNum * pagesize 监测newsList size变化,必要时将关键词置为 ""
             */
            int curSize = newsList.size();
            keywordsList = new ArrayList<>();
            keywordsList.addAll(historyDao.getHistoryBrowseKeyWords());
            keywordsList.addAll(collectionsDao.getHistoryBrowseKeyWords());
            System.out.println(keywordsList);

            keywordsIndex = 0;
        }{
            this.queryWords = this.words;
            this.queryCategory = this.category;
        }
        recursionDepth = 0;
        connect();
    }
    private void showErrorMessage(String title, String message){
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorTitle.setText(title);
        errorMessage.setText(message);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum = 1;
                request();
            }
        });
    }
}
