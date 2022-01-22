package com.precious.moro.Models;

import java.util.HashMap;
import java.util.List;

public class Follower {
    private HashMap<String,String> followers;
    private String UserID;
    private String userName;
    private String ImageProfile;

    public Follower(HashMap<String,String>followers,String UserID,String userName,String ImageProfile){
        this.followers = followers;
        this.UserID=UserID;
        this.userName=userName;
        this.ImageProfile=ImageProfile;

    }
    public HashMap<String,String> getFollowers(){
        return followers;
    }
    public void setFollowers(HashMap<String,String> followers){
        this.followers= followers;
    }
    public String getUserID(){return UserID;}
    public void setUserID(String UserID){this.UserID=UserID;}
    public String getUserName(){return userName;}
    public void setUserName(String userName){this.userName=userName;}
    public String getImageProfile(){return ImageProfile;}
    public void setImageProfile(String ImageProfile){this.ImageProfile=ImageProfile;}

    public boolean following(String UserID){
        if(followers.containsKey(UserID)==true){
            return true;
        }
        else{
            return false;
        }
    }
}

