package com.precious.moro.Models;

import android.net.Uri;

import java.util.List;

public class Videos {
    private String Caption;
    private String Uri;
    private String UserId;
    private String id;
    public Videos(){}

    public Videos(String Caption,String Url,String UserId,String id){
        this.Caption=Caption;
        this.id=id;
        this.Uri=Uri;
        this.UserId=UserId;
    }

    public String getCaption(){
        return Caption;
    }
    public String getid(){
        return id;
    }

    public String getUri(){
        return Uri;
    }
    public String getUserId(){return UserId;}

    public void setCaption(String Caption){
        this.Caption= Caption;
    }
    public void setid(String id){
        this.id= id;
    }
    public void setUrl(String Uri){ this.Uri=Uri; }
    public void setUserId(String UserId){
        this.UserId=UserId;
    }
}
