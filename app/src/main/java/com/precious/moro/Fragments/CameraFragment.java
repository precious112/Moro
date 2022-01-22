package com.precious.moro.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseUser;
import com.precious.moro.Interfaces.VideoUploadCallBack;
import com.precious.moro.MainActivity;
import com.precious.moro.Models.Videos;
import com.precious.moro.R;
import com.precious.moro.ViewModels.VideoUploadViewModel;
import com.precious.moro.databinding.FragmentCameraBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements VideoUploadCallBack {
    private FragmentCameraBinding binding;
    private FirebaseUser firebaseUser;
    private Uri VideoUri;
    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private String[] cameraPermissions;
    MediaController mediaController;
    public ProgressDialog progressDialog;
    private VideoUploadViewModel videoUploadViewModel;
    private Videos videos;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container2,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        cameraPermissions= new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_camera,container2,false);
        videoUploadViewModel=new ViewModelProvider(this).get(VideoUploadViewModel.class);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("please wait...");
        progressDialog.setMessage("Uploading Video");
        progressDialog.setCanceledOnTouchOutside(false);
        binding.choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPickDialog();
            }
        });

        binding.uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              progressDialog.show();
             String caption = binding.VideoCaption.toString();

             videoUploadViewModel.setVideoUri(VideoUri);
             videoUploadViewModel.setCaption(caption);
             videoUploadViewModel.init(getActivity());



            }
        });
        return binding.getRoot();
    }
    private void videoPickDialog(){
        String[] options={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Pick A Video From").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                   if(!checkCameraPermission()){
                       requestCameraPermission();
                   }else{
                       videoPickCamera();
                   }
                } else if(which==1){
                   videoPickGallery();
                }
            }
        }).show();
    }
    private void requestCameraPermission(){
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);

    }
    private boolean checkCameraPermission(){
        boolean result1= ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean result2= ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WAKE_LOCK)== PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
    }
    private void videoPickGallery(){
        Intent intent= new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select videos"),VIDEO_PICK_CAMERA_CODE);
    }
    private void videoPickCamera(){
        Intent intent= new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,VIDEO_PICK_CAMERA_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted&&storageAccepted){
                        videoPickCamera();
                    } else{
                        Toast.makeText(getContext(), "Camera && Storage permission required", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    private void setVideoToVideoView(){
        mediaController=new MediaController(getContext());

        binding.VideoViewUploader.setMediaController(mediaController);
        binding.VideoViewUploader.setVideoURI(VideoUri);
        binding.VideoViewUploader.requestFocus();
        binding.VideoViewUploader.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaController.setAnchorView(binding.VideoViewUploader);
                binding.VideoViewUploader.pause();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){
        if(resultCode==getActivity().RESULT_OK){
            if(requestCode==VIDEO_PICK_GALLERY_CODE){
                VideoUri=data.getData();
                setVideoToVideoView();
            } else if(requestCode==VIDEO_PICK_CAMERA_CODE){
                VideoUri=data.getData();
                setVideoToVideoView();
            }
        }
    }

    @Override
    public void videoCallBack() {
        progressDialog.dismiss();
        Toast.makeText(getContext(),"Video Uploaded Successfilly!",Toast.LENGTH_SHORT).show();
    }
}