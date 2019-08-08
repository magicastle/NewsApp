package com.example.newsapp.model;

import android.provider.ContactsContract;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SingleNews {
    @SerializedName("publishTime")
    private String publishTime;
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("language")
    private String language;
    @SerializedName("image")
    private String image;
    @SerializedName("video")
    private String video;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("newsID")
    private String newsID;
    @SerializedName("category")
    private String category;

    @SerializedName("persons")
    private ArrayList<Persons> persons;
    @SerializedName("locations")
    private ArrayList<Locations> locations;
    @SerializedName("organizations")
    private ArrayList<Organizations> organizations;

    @SerializedName("keywords")
    private ArrayList<ScoreAndWord> keyWords;
    @SerializedName("when")
    private ArrayList<ScoreAndWord> when;
    @SerializedName("where")
    private ArrayList<ScoreAndWord> where;
    @SerializedName("who")
    private ArrayList<ScoreAndWord> who;


    SingleNews(
            String publishTime,
            String publisher,
            String language,
            String image,
            String video,
            String title,
            String content,
            String newsID,
            String category,

            ArrayList<Persons> persons,
            ArrayList<Locations> locations,
            ArrayList<Organizations> organzations,

            ArrayList<ScoreAndWord> when,
            ArrayList<ScoreAndWord> keyWords,
            ArrayList<ScoreAndWord> where,
            ArrayList<ScoreAndWord> who
    ){
        this.publishTime = publishTime;
        this.publisher = publisher;
        this.language = language;
        this.image = image;
        this.video = video;
        this.title = title;
        this.content = content;
        this.newsID = newsID;
        this.category = category;

        this.persons = persons;
        this.locations = locations;
        this.organizations = organzations;

        this.keyWords = keyWords;
        this.when = when;
        this.where = where;
        this.who = who;
    }

    public String getContent() {
        return content;
    }
}

class ScoreAndWord{
    @SerializedName("score")
    private float score;
    @SerializedName("word")
    private String word;

    ScoreAndWord(float score, String word){
        this.score = score;
        this.word = word;
    }

}

class Persons{
    @SerializedName("count")
    private Integer count;
    @SerializedName("linkedURL")
    private String linkedURL;
    @SerializedName("mention")
    private String mention;

    Persons(Integer count, String linkedURL, String mention){
        this.count = count;
        this.linkedURL = linkedURL;
        this.mention = mention;
    }
}

class Organizations{
    @SerializedName("count")
    private Integer count;
    @SerializedName("linkedURL")
    private String linkedURL;
    @SerializedName("mention")
    private String mention;

    Organizations(Integer count, String linkedURL, String mention){
        this.count = count;
        this.linkedURL = linkedURL;
        this.mention = mention;
    }
}

class Locations{
    @SerializedName("lng")
    private float lng;
    @SerializedName("count")
    private Integer count;
    @SerializedName("linkedURL")
    private String linkedURL;
    @SerializedName("lat")
    private float lat;
    @SerializedName("mention")
    private String mention;

    Locations(float lng, Integer count, String linkedURL, float lat, String mention){
        this.lng = lng;
        this.count = count;
        this.linkedURL = linkedURL;
        this.lat = lat;
        this.mention = mention;
    }
}
