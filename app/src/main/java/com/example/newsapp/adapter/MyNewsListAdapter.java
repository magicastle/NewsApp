package com.example.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.MainActivity;
import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.model.SingleNews;
import com.example.newsapp.util.Constant;

import java.util.List;

public class MyNewsListAdapter extends RecyclerView.Adapter<MyNewsListAdapter.MyViewHolder> {
    private List<SingleNews> newsList;
    private Context context;

    public MyNewsListAdapter(List<SingleNews> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView contentAbstract;
        TextView publishInfo;
//        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.item_newsTitle);
            contentAbstract = itemView.findViewById(R.id.item_newsAbstract);
            publishInfo = itemView.findViewById(R.id.item_newsPublishInfo);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    // MainActivity.mainActivity 为定义的static常量，不然的话从Mainactivity -> NewsRecycleView(Fragment) -> Adapter -> here 一层层传实在太烦了
                    Intent intent = new Intent(MainActivity.mainActivity, NewsDetailActivity.class);
                    SingleNews newsDetail = newsList.get(getAdapterPosition());
                    intent.putExtra("newsDetail", newsDetail);

                    // start newsDetail page
                    // context 为构造时传入的adapter所在activity/fragment的context
                    context.startActivity(intent);
                }
            });

        }
    }

    @NonNull
    @Override
    public MyNewsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_news_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        SingleNews news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.contentAbstract.setText(news.getAbstract());
        holder.publishInfo.setText("  " + news.getPublisher() + "     " + news.getPublishTime());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(position + 1 == getItemCount()){
//            return Constant.ITEM_TYPE_FOOT_VIEW;
//        }
//        return Constant.ITEM_TYPE_SINGLE_NEWS;
//    }
}
