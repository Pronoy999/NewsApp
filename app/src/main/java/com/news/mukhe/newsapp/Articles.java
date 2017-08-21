package com.news.mukhe.newsapp;

/**
 * Created by mukhe on 21-Aug-17.
 */

public class Articles {
    private String title,description,url;
    public Articles(String title,String description,String url){
        this.title=title;
        this.description=description;
        this.url=url;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getUrl(){
        return url;
    }
}
