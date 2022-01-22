package com.precious.moro.Models;

import com.precious.moro.Models.Videos;

import java.util.List;

public class VideoList {
    private String UserId;
    private List<Videos> VideosList;
    public VideoList(){}
    public VideoList(String UserId,List<Videos>VideosList){
        this.UserId=UserId;
        this.VideosList=VideosList;
    }

    public String getUserId(){
        return UserId;
    }
    public List<Videos> getVideosList(){
        return VideosList;
    }
    public void setUserId(String UserId){this.UserId=UserId;}
    public void setVideosList(List<Videos> VideosList){
        this.VideosList=VideosList;
    }
}
