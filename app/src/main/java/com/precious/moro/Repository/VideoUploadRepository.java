package com.precious.moro.Repository;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.precious.moro.Fragments.CameraFragment;
import com.precious.moro.Interfaces.VideoUploadCallBack;
import com.precious.moro.Models.VideoList;
import com.precious.moro.Models.Videos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VideoUploadRepository {
    private static VideoUploadRepository instance;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private final MutableLiveData<Boolean> isLoading=new MutableLiveData<>();

    static Context mcontext;
    static VideoUploadCallBack videoUploadCallBack;
    public static VideoUploadRepository getInstance(Context context){
        mcontext=context;
        if(instance == null){
            instance = new VideoUploadRepository();
        }
        videoUploadCallBack=(VideoUploadCallBack) mcontext;
        return instance;
    }


    public void UploadVideo(MutableLiveData<Uri> videoUri,MutableLiveData<String> Caption){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        upload(videoUri.getValue(),Caption.getValue());
    }
    private void upload(Uri videoUri,String Caption){
        String timestamp= ""+System.currentTimeMillis();
       String filePathAndName= "Videos/" + "videos_" + timestamp;
        Uri uri= videoUri;
        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri downloadUri=uriTask.getResult();

                if(uriTask.isSuccessful()){
                    HashMap<String,Object> map= new HashMap<>();
                    map.put("UserId",""+firebaseUser.getUid());
                    map.put("id",""+timestamp);
                    map.put("Caption",""+Caption);
                    map.put("Uri",""+downloadUri);
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Videos");
                    reference.child(timestamp).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           videoUploadCallBack.videoCallBack();
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
