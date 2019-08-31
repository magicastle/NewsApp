package com.example.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newsapp.MainActivity;
import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.bean.NewsCollectionsOrHistoryBean;
import com.example.newsapp.database.NewsCollectionsDao;
import com.example.newsapp.database.NewsHistoryDao;
import com.example.newsapp.model.SingleNews;
import com.example.newsapp.util.Constant;
import com.example.newsapp.util.SelectedItem;

import java.util.List;

public class MyNewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SingleNews> newsList;
    private Context context;
    private NewsHistoryDao historyDao = new NewsHistoryDao();

    public MyNewsListAdapter(List<SingleNews> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView contentAbstract;
        TextView publishInfo;
        ImageView image_right;
        boolean isAdded;

        public MyViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.item_newsTitle);
            contentAbstract = itemView.findViewById(R.id.item_newsAbstract);
            publishInfo = itemView.findViewById(R.id.item_newsPublishInfo);
            image_right = itemView.findViewById(R.id.image_right);
            isAdded = false;

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    // MainActivity.mainActivity 为定义的static常量，不然的话从Mainactivity -> NewsRecycleView(Fragment) -> Adapter -> here 一层层传实在太烦了
                    Intent intent = new Intent(MainActivity.mainActivity, NewsDetailActivity.class);
                    SingleNews news= newsList.get(getAdapterPosition());
                    intent.putExtra("news", news);

                    // 为了标识点击事件
                    // SelectedItem.setSelectedNavItem(getAdapterPosition());
                    // notifyDataSetChanged();

                    // 点击新闻即加入到 History 数据库中
                    historyDao.add(news.getNewsID(),
                            news.getImageString(),
                            news.getPublishTime(),
                            news.getPublisher(),
                            news.getTitle(),
                            news.getContent()
                    );

                    // start newsDetail page
                    // context 为构造时传入的adapter所在activity/fragment的context
                    context.startActivity(intent);
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context)
                .inflate(R.layout.item_news_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holders, int position) {
        MyViewHolder holder = (MyViewHolder) holders;
        if(holder.isAdded == false){
                SingleNews news = newsList.get(position);
                holder.title.setText(news.getTitle());
                holder.contentAbstract.setText(news.getAbstract());
                holder.publishInfo.setText("  " + news.getPublisher() + "     " + news.getPublishTime());

                String urls[]= news.getImage();//images lists

                if(urls.length>0)
                {
                    System.out.println(news.getTitle()+" "+urls[0]);

                    RequestListener mRequestListener = new RequestListener() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                            Log.d("NewsDetailActivity", "onException: " + e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
                            holder.image_right.setImageResource(R.mipmap.ic_launcher);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                            Log.e("NewsDetailActivity",  "model:"+model+" isFirstResource: "+isFirstResource);
                            return false;
                        }
                    };
                    RequestOptions options = new RequestOptions()
                            //.centerCrop()
                            .placeholder(new ColorDrawable(Color.BLACK))
                            .error(new ColorDrawable(Color.RED))
                            .centerCrop()
                            .priority(Priority.HIGH);

                    Glide.with(context.getApplicationContext())
                            .load( urls[0])
                            .apply(options)
                            .listener(mRequestListener)
                            .into(holder.image_right);
                    System.out.println("load....");
                }
                else{
                    System.out.println(news.getTitle()+"  else......");
                    holder.image_right.setVisibility(View.GONE);
                }

                holder.isAdded = true;
        }

//        // TODO: 查看是否已经在History数据库中，如果有，就 Bina 灰 View
//        List<NewsCollectionsOrHistoryBean> list = historyDao.query(news.getNewsID());
//        if(list.size() == 0){
//            //
//        }


        // TODO: 点击效果
        // 设置点击效果
        // if(position == SelectedItem.getSelectedItemPosition()){
        //     holder.itemView.setBackgroundColor(MainActivity.globalContext.getResources().getColor(R.color.grey));
        // }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position;
    }
}
