package com.example.newsapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsapp.bean.NewsCollectionsOrHistoryBean;

import java.util.ArrayList;
import java.util.List;

public class NewsHistoryDao {

    private SQLiteDatabase db;
    public NewsHistoryDao(){
        this.db = DatabaseHelper.getDatabase();
    }

    public void addInitData(){
    }

    public boolean add(String newsID, String image, String publishTime, String publisher, String title, String content){
        ContentValues values = new ContentValues();
        values.put(NewsHistoryTable.ID, newsID);
        values.put(NewsHistoryTable.IMAGE, image);
        values.put(NewsHistoryTable.PUBLISHTIME, publishTime);
        values.put(NewsHistoryTable.PUBLISHER, publisher);
        values.put(NewsHistoryTable.TITLE, title);
        values.put(NewsHistoryTable.CONTENT, content);
        long result = db.insertWithOnConflict(NewsHistoryTable.TABLENAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public List<NewsCollectionsOrHistoryBean> query() {
        Cursor cursor = db.query(NewsHistoryTable.TABLENAME, null, null, null, null, null, null);
        List<NewsCollectionsOrHistoryBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            NewsCollectionsOrHistoryBean bean = new NewsCollectionsOrHistoryBean();
            bean.setNewsID(cursor.getString(NewsHistoryTable.ID_ID));
            bean.setImage(cursor.getString(NewsHistoryTable.ID_IMAGE));
            bean.setPublishTime(cursor.getString(NewsHistoryTable.ID_PUBLISHTIME));
            bean.setPublisher(cursor.getString(NewsHistoryTable.ID_PUBLISHER));
            bean.setTitle(cursor.getString(NewsHistoryTable.ID_TITLE));
            bean.setContent(cursor.getString(NewsHistoryTable.ID_CONSTANT));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    public void updateAll(List<NewsCollectionsOrHistoryBean> list) {
    }

    public boolean removeAll() {
        int result = db.delete(NewsHistoryTable.TABLENAME, null, null);
        return result != -1;
    }
}
