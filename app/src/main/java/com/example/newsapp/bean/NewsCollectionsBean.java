package com.example.newsapp.bean;

import androidx.annotation.NonNull;

import com.example.newsapp.database.NewsCollectionsTable;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsCollectionsBean {

    private String image;
    private String publishTime;
    private String publisher;
    private String title;
    private String content;



    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    public void setContent(String content){
        this.content = content;
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

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public String getPublisher(){return this.publisher;}

    public void setPublishTime(String publishTime){
        this.publishTime = publishTime;
    }
    public String getPublishTime(){return this.publishTime;}

    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
    //public String[] getImage()
    //{
    //    //System.out.println("image is "+image);
    //    image=image.substring(1,image.length()-1);
    //    //System.out.println("image iss "+image);
    //    String[] images;
    //    if(image.length()>0)
    //        images=image.split(",");
    //    else images=new String[0];
    //    System.out.println("images.length="+images.length);
    //    System.out.println(Arrays.asList(images));
    //    return images;
    //}
}
