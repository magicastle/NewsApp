package com.example.newsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.newsapp.adapter.MyPagerAdapter;
import com.example.newsapp.bean.NewsChannelBean;
import com.example.newsapp.database.NewsChannelDao;
import com.example.newsapp.fragment.NewsRecycleView;
import com.example.newsapp.util.Constant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String words = "";

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView modifyNewsChannel;
    private List<String> titles;
    private List<Fragment> fragments;
    private MyPagerAdapter myPagerAdapter;

    public static Context globalContext;
    private NewsChannelDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalContext = getApplicationContext();
        // initData 需要在 initView 之前完成，这样才知道要有多少个tab(fragment)需要建立
        initData();
        initView();
    }

    public void initView(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = findViewById(R.id.tab_layout_news);
        viewPager = findViewById(R.id.view_pager_news);
        modifyNewsChannel = findViewById(R.id.modify_channel_iv);
        modifyNewsChannel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewsChannelActivity.class));
            }
        });

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void initData(){
        dao = new NewsChannelDao();
        titles = new ArrayList<>();
        fragments = new ArrayList<>();

        // TODO: categoryName - fragment hashmap
        List<NewsChannelBean> channelList = dao.query(Constant.NEWS_CHANNEL_ENABLE);
        titles = new ArrayList<>();
        if (channelList.size() == 0) {
            dao.addInitData();
            channelList = dao.query(Constant.NEWS_CHANNEL_ENABLE);
        }


        for (NewsChannelBean bean : channelList) {
            titles.add(bean.getChannelName());
            NewsRecycleView nrc = new NewsRecycleView();
            Bundle bundle = new Bundle();
            bundle.putString("words", this.words);
            bundle.putString("category", bean.getChannelName());
            nrc.setArguments(bundle);
            fragments.add(nrc);
        }


//        titles.add("娱乐"); titles.add("军事"); titles.add("教育"); titles.add("文化"); titles.add("健康");
//        titles.add("财经"); titles.add("体育"); titles.add("汽车"); titles.add("科技"); titles.add("社会");
//        for(int i = 0;i < titles.size();i ++){
//            // TODO : 推荐频道
//            NewsRecycleView nrc = new NewsRecycleView();
//            Bundle bundle = new Bundle();
//            bundle.putString("words", this.words);
//            bundle.putString("category", titles.get(i));
//            nrc.setArguments(bundle);
//            fragments.add(nrc);
//        }
    }

    public void updateNewsTabs(){
        fragments.clear();
        System.out.println("titles size " + titles.size());
        for(int i = 0;i < titles.size();i ++){
            // TODO : 推荐频道
            NewsRecycleView nrc = new NewsRecycleView();
            Bundle bundle = new Bundle();
            System.out.println(this.words);
            bundle.putString("words", this.words);
            bundle.putString("category", titles.get(i));
            nrc.setArguments(bundle);
            fragments.add(nrc);
        }
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(myPagerAdapter);
    }

    // below : code come with template
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // searchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                if (query.length() > 1){
                    MainActivity.this.words = query;
                    updateNewsTabs();
                }
                else {
                    Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_news_list clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item_news_list clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
