package com.precious.moro.ViewModels;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.precious.moro.Models.Videos;
import com.precious.moro.Repository.VideoUploadRepository;

public class VideoUploadViewModel extends ViewModel {
    private MutableLiveData<Uri> VideoUri=new MutableLiveData<>();
    private MutableLiveData<String> Caption=new MutableLiveData<>();
    private VideoUploadRepository mRepo;
    public void init(Context context){
          mRepo=VideoUploadRepository.getInstance(context);
          mRepo.UploadVideo(VideoUri,Caption);
    }
    public MutableLiveData<Uri> getVideoUri(){
        return VideoUri;
    }
    public MutableLiveData<String> getCaption(){return Caption;}


    public void setVideoUri(Uri video) {
        VideoUri.setValue(video);
    }
    public void setCaption(String Caption){ this.Caption.setValue(Caption); }
}
