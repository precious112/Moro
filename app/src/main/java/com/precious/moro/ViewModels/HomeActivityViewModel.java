package com.precious.moro.ViewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.precious.moro.Models.Videos;
import com.precious.moro.Repository.HomeActivityRepository;

import java.util.List;

public class HomeActivityViewModel extends ViewModel {
    private MutableLiveData<List<Videos>> videos=new MutableLiveData<>();
    private HomeActivityRepository mRepo;
    public void init(Context context){
        mRepo=HomeActivityRepository.getInstance(context);
        videos.setValue(mRepo.getVideos().getValue());
    }
    public MutableLiveData<List<Videos>> getVideos(){
        return videos;
    }
}
