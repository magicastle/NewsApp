package com.example.newsapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.MyNewsListAdapter;
import com.example.newsapp.model.NewsData;
import com.example.newsapp.model.SingleNews;
import com.example.newsapp.network.GetDataService;
import com.example.newsapp.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRecycleView extends Fragment {
    private View view;
    // search options
    private String size = "20";
    private String startDate = "2019-07-01 13:12:45";
    private String endDate   = "2021-08-03 18:42:20";
    private String words = "";
    private String category = "";
    private String page="1";

    private ProgressDialog progressDialog;
    private NewsData newsData;
    private List<SingleNews> newsList;
    private RecyclerView recyclerView;
    private MyNewsListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        Bundle bundle = getArguments();
        words = bundle.getString("words");
        category = bundle.getString("category");
        initView();
        request();
        System.out.println("new build fragment news list: " + words + " " + category);
        return view;
    }

    public void initView(){
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        newsList = new ArrayList<SingleNews>();
        mAdapter = new MyNewsListAdapter(newsList, getActivity());
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        //swipeRefreshLayout.setColorSchemeColors();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsList.clear();
                request();
                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // TODO: 让加载操作位于新线程中
    public void request(){
        //progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("Loading....");
        //progressDialog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NewsData> call;
        call = service.getNewsSearch(
                this.size,
                this.startDate,
                this.endDate,
                this.words,
                this.category,
                this.page
        );
        call.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                //progressDialog.dismiss();
                newsList.addAll(response.body().getData());
                mAdapter.notifyDataSetChanged();
                initListener();
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                // progressDialog.dismiss();
                // TODO: load error activity
                Toast.makeText(getActivity(), "Load error.... maybe retry....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initListener(){
        mAdapter.setOnItemClickListener(new MyNewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // prepare the data pasted from MainActivity to NewsDetail page
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                SingleNews newsDetail = newsList.get(position);
                intent.putExtra("newsDetail", newsDetail);

                // start newsDetail page
                startActivity(intent);
            }
        });
    }
}
