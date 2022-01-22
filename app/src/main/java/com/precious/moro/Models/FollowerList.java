package com.precious.moro.Models;

import java.util.HashMap;

public class FollowerList {
    private HashMap<String,String>followersList;
    public FollowerList(){}
    public FollowerList(HashMap<String,String>followersList){
        this.followersList=followersList;
    }
    public HashMap<String,String> getFollowersList(){
        return followersList;
    }
    public void setFollowersList(HashMap<String,String> followersList){
        this.followersList= followersList;
    }
}
