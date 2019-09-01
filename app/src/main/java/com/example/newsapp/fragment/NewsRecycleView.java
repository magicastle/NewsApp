package com.example.newsapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    // category: record newslist category
    private String category = "";
    // queryCategory: used to query really
    private String queryCategory = "";
    private Integer page = 1;
    // judge whether be the end
    private Boolean onEarth = false;

    private ProgressDialog progressDialog;
    private NewsData newsData;
    private List<SingleNews> newsList;
    private RecyclerView recyclerView;
    private MyNewsListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout errorLayout;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;

    //private SwipeRefreshLayout swipeRefreshLayout;
    private EasyRefreshLayout easyRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news_list, container, false);

        // init search options
        Bundle bundle = getArguments();
        words = bundle.getString("words");
        category = bundle.getString("category");
        queryCategory = category;
        page = 1;

        initView();
        request();
        System.out.println("new build fragment news list: " + words + " " + category);
        return view;
    }

    public void initView(){
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(layoutManager);
        newsList = new ArrayList<SingleNews>();
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
    }

    // TODO: 让加载操作位于新线程中
    public void request(){
        errorLayout.setVisibility(View.GONE);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NewsData> call;
        if(this.category.equals("推荐")){
            this.queryCategory = "军事";
        }
        call = service.getNewsSearch(
                this.size,
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
                    if(response.body().getData().size() == 0){
                        Toast.makeText(getActivity(), "On Earth !!", Toast.LENGTH_SHORT).show();
                        onEarth = true;
                        return;
                    }
                    // 推荐栏目下，每次request只向newslist添加最多两条消息
                    if(category.equals("推荐")){
                        newsList.addAll(response.body().getData().subList(0, Math.min(3, response.body().getData().size())));
                    }
                    else{
                        newsList.addAll(response.body().getData());
                    }
                    mAdapter.notifyDataSetChanged();
                }

//              // error view
//                else{
//                    // 为了实现当已加载部分列表时，重新加载即使失败，当前列表也会保留，所以添加了if-else控制
//                    if(newsList.size() == 0){
//                        String errorCode;
//                        switch (response.code()) {
//                            case 404:
//                                errorCode = "404 not found";
//                                break;
//                            case 500:
//                                errorCode = "500 server broken";
//                                break;
//                            default:
//                                errorCode = "unknown error";
//                                break;
//                        }
//                        showErrorMessage(
//                                "No Result",
//                                "Please Try Again!\n"+
//                                        errorCode);
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
//                if(newsList.size() == 0){
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


    private void showErrorMessage(String title, String message){
        System.out.println("showErrorMessage");

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        System.out.println("showErrorMessage");
        errorTitle.setText(title);
        System.out.println("showErrorMessage");
        errorMessage.setText(message);
        System.out.println("showErrorMessage");

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                request();
            }
        });
    }
}
