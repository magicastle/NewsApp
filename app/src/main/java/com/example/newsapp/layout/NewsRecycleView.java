package com.example.newsapp.layout;

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

import com.example.newsapp.MainActivity;
import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.MyAdapter;
import com.example.newsapp.model.NewsData;
import com.example.newsapp.model.SingleNews;
import com.example.newsapp.network.GetDataService;
import com.example.newsapp.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRecycleView extends Fragment {
    private View view;
    // search options
    // TODO: make search options could change
    private String size = "20";
    private String startDate = "2019-07-01 13:12:45";
    private String endDate   = "2021-08-03 18:42:20";
    private String words = "";
    private String category = "";
    private String page="2";

    private ProgressDialog progressDialog;
    private NewsData newsData;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        Bundle bundle = getArguments();
        category = bundle.getString("category");

        initView();
        request();
        return view;
    }

    public void initView(){
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    public void request(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
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
                progressDialog.dismiss();
                newsData = response.body();
                mAdapter = new MyAdapter(newsData, getActivity());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                initListener();
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                progressDialog.dismiss();
                // TODO: load error activity
                Toast.makeText(getActivity(), "Load error.... maybe retry....", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void initListener(){
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // prepare the data pasted from MainActivity to NewsDetail page
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                SingleNews newsDetail = newsData.getData().get(position);
                intent.putExtra("newsDetail", newsDetail);

                // start newsDetail page
                startActivity(intent);
            }
        });
    }
}
