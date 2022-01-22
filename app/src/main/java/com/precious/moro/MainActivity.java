package com.precious.moro;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;



import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.precious.moro.Adapters.HomeFragmentAdapter;
import com.precious.moro.Fragments.CameraFragment;
import com.precious.moro.Fragments.HomeFragment;
import com.precious.moro.Fragments.SearchFragment;
import com.precious.moro.Interfaces.SearchCallBack;
import com.precious.moro.Interfaces.VideoUploadCallBack;
import com.precious.moro.Interfaces.VideosCallBack;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements VideosCallBack, SearchCallBack, VideoUploadCallBack {
    private FirebaseUser firebaseUser;
    BottomNavigationView bottomNavigationView;

    private VideosCallBack callback;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof VideosCallBack) {
            callback = (VideosCallBack) fragment;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        bottomNavigationView.setSelectedItemId(R.id.home);

    }
    HomeFragment HomeFragment = new HomeFragment();
    SearchFragment SearchFragment = new SearchFragment();
    CameraFragment CameraFragment = new CameraFragment();


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, HomeFragment).commit();
                return true;

            case R.id.search:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, SearchFragment).commit();
                return true;

            case R.id.camera:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, CameraFragment).commit();
                return true;

            case R.id.profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                return true;
        }
        return false;
    }


    @Override
    public void videoCallBacks() {
        HomeFragment.videoCallBacks();
    }

    @Override
    public void searchCallBacks() {
        SearchFragment.searchCallBacks();
    }

    @Override
    public void videoCallBack() {
        CameraFragment.videoCallBack();

    }
}
