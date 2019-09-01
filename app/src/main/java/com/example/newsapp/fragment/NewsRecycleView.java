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
    private String category = "";
    private String queryCategory = "";
    private Integer page = 1;
    // judge whether be the end
    private Boolean onEarth = false;
//    private Boolean requestSuccessful = false;

    private ProgressDialog progressDialog;
    private NewsData newsData;
    private List<SingleNews> newsList;
    private HashSet<String> newsIDSet;
    private RecyclerView recyclerView;
    private MyNewsListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout errorLayout;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;
    private EasyRefreshLayout easyRefreshLayout;
    private NewsHistoryDao historyDao = new NewsHistoryDao();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news_list, container, false);

        // init search options
        Bundle bundle = getArguments();
        words = bundle.getString("words");
        category = bundle.getString("category");
        page = 1;

        initView();
        request();
        return view;
    }

    public NewsRecycleView(){}
    public void initView(){
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        newsList = new ArrayList<>();
        newsIDSet = new HashSet<>();
        mAdapter = new MyNewsListAdapter(newsList, getActivity());
        recyclerView.setAdapter(mAdapter);

        easyRefreshLayout = view.findViewById(R.id.easy_refresh_layout);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                if(onEarth == false){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            page ++;
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
                        page = 1;
                        request();
                        easyRefreshLayout.refreshComplete();
                        Toast.makeText(getActivity(), "Refreshed!", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });

        errorLayout = view.findViewById(R.id.errorLayout);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        errorLayout.setVisibility(View.GONE);
    }
    public void connect(){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NewsData> call;
        call = service.getNewsSearch(
                this.pageSize.toString(),
                this.startDate,
                this.endDate,
                this.words,
                this.queryCategory,
                this.page.toString()
        );
        call.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                if(response.isSuccessful()){
                    List<SingleNews> reqSingleNewsList = response.body().getData();

                    // no more news data, update the "onEarth" flag
                    if(reqSingleNewsList.size() == 0){
                        Toast.makeText(getActivity(), "On Earth !!", Toast.LENGTH_SHORT).show();
                        onEarth = true;
                        return;
                    }
                    // 推荐栏目下，每次request只向newslist添加最多2条消息
                    if(category.equals("推荐")){
                        // 避免推荐列表重复，添加newsIDSet control
                        for (SingleNews news : reqSingleNewsList.subList(0, Math.min(3, response.body().getData().size()))){
                            /*
                                添加条件：
                                1. 当前列表不重复
                                2. 未点击看过
                                3. 新闻数量未满
                             */
                            String id = news.getNewsID();
                            if(!newsIDSet.contains(id) && !historyDao.contain(id) && newsList.size() < page*pageSize){
                                newsIDSet.add(id);
                                newsList.add(news);
                            }
                        }
                    }
                    else{
                        newsList.addAll(reqSingleNewsList);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
//                if(newsList.pageSize() == 0){
//                    showErrorMessage(
//                            "Oops..",
//                            "Network failure, Please Try Again\n"+
//                                    t.toString());
//                }
//                else{
//                    Toast.makeText(getActivity(), "Load error... maybe retry...", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(getActivity(), "Load error... maybe retry...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void request(){
        if(this.category.equals("推荐")){
            this.queryCategory = "";
            connect();
        }{
            connect();
        }
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
                page = 1;
                request();
            }
        });
    }
}
