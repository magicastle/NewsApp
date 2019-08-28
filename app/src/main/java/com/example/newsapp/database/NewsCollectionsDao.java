package com.example.newsapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsapp.bean.NewsCollectionsBean;

import java.util.ArrayList;
import java.util.List;

public class NewsCollectionsDao {

    private SQLiteDatabase db;
    public  NewsCollectionsDao(){
        this.db = DatabaseHelper.getDatabase();
    }

    public void addInitData(){
    }

    public boolean add(String image, String publishTime, String publisher, String title, String content){
        ContentValues values = new ContentValues();
        values.put(NewsCollectionsTable.IMAGE, image);
        values.put(NewsCollectionsTable.PUBLISHTIME, publishTime);
        values.put(NewsCollectionsTable.PUBLISHER, publisher);
        values.put(NewsCollectionsTable.TITLE, title);
        values.put(NewsCollectionsTable.CONTENT, content);
        long result = db.insert(NewsCollectionsTable.TABLENAME, null, values);
        return result != -1;
    }

    public List<NewsCollectionsBean> query() {
        Cursor cursor = db.query(NewsCollectionsTable.TABLENAME, null, null, null, null, null, null);
        List<NewsCollectionsBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            NewsCollectionsBean bean = new NewsCollectionsBean();
            bean.setImage(cursor.getString(NewsCollectionsTable.ID_IMAGE));
            bean.setPublishTime(cursor.getString(NewsCollectionsTable.ID_PUBLISHTIME));
            bean.setPublisher(cursor.getString(NewsCollectionsTable.ID_PUBLISHER));
            bean.setTitle(cursor.getString(NewsCollectionsTable.ID_TITLE));
            bean.setContent(cursor.getString(NewsCollectionsTable.ID_CONSTANT));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    public void updateAll(List<NewsCollectionsBean> list) {
    }

    public boolean removeAll() {
        int result = db.delete(NewsCollectionsTable.TABLENAME, null, null);
        return result != -1;
    }
}
