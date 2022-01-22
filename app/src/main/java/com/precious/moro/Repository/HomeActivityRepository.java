package com.precious.moro.Repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.precious.moro.Interfaces.VideosCallBack;
import com.precious.moro.Models.VideoList;
import com.precious.moro.Models.Videos;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityRepository {
    private static HomeActivityRepository instance;
    private List<Videos> videoList=new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseDatabase firebaseDatabase;
    private Videos video=new Videos();

    static Context mcontext;
    static VideosCallBack mVideosCallBack;
    public static HomeActivityRepository getInstance(Context context){
        mcontext=context;
        if(instance == null){
            instance = new HomeActivityRepository();
        }
        mVideosCallBack= (VideosCallBack) mcontext;

        return instance;
    }
    public MutableLiveData<List<Videos>> getVideos(){

        MutableLiveData<List<Videos>> data=new MutableLiveData<>();
        queryVideos();
        data.setValue(videoList);

        return data;

    }
    private void queryVideos(){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Videos");
        Query query=reference.orderByChild("UserId").equalTo(firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    video=Snapshot.getValue(Videos.class);
                    videoList.add(video);

                }
                mVideosCallBack.videoCallBacks();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
