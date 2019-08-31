package com.example.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.URL;


public class SingleNews implements Serializable {
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

    @SerializedName("url")
    private String url;

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


    public SingleNews(
            String image,
            String publishTime,
            String publisher,
            String title,
            String content,

            String language,
            String video,
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

    public String getTitle() {
        return this.title;
    }
    public String getContent() {
        return this.content;
    }
    public String getAbstract(){
        String a = this.content.substring(0, Math.min(this.content.length(), 110)) + "...";
        Pattern p = Pattern.compile("(\\s|\n)");
        Matcher m = p.matcher(a);
        a = m.replaceAll("");
        return a;
    }
    public String getPublisher(){return this.publisher;}
    public String getPublishTime(){return this.publishTime;}
    public String[] getImage()
    {
        System.out.println("image is "+image);
        image=image.substring(1,image.length()-1);
        String[] images;//
        if(image.length()>0) {
            image = image.replaceAll("\\]\\s+\\[", ",");
            images = image.split(",");
        }
        else images=new String[0];
        System.out.println("images.length="+images.length);

        return images;
    }
    public  String getUrl(){return this.url;}

}

class ScoreAndWord implements Serializable{
    @SerializedName("score")
    private float score;
    @SerializedName("word")
    private String word;

    ScoreAndWord(float score, String word){
        this.score = score;
        this.word = word;
    }

}

class Persons implements Serializable{
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

class Organizations implements Serializable{
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

class Locations implements Serializable{
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
