package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.model.NewsData;
import com.example.newsapp.model.SingleNews;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<SingleNews> newsList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    //public MyAdapter(NewsData newsData, Context context){
    public MyAdapter(List<SingleNews> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView contentAbstract;
        TextView publishInfo;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);

            title = itemView.findViewById(R.id.item_newsTitle);
            contentAbstract = itemView.findViewById(R.id.item_newsAbstract);
            publishInfo = itemView.findViewById(R.id.item_newsPublishInfo);

            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(view, onItemClickListener);
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
