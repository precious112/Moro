package com.precious.moro;


import androidx.appcompat.app.AppCompatActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;



import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private final int ID_HOME=1;
    private final int ID_MESSAGE=2;
    private final int ID_NOTIFICATION=3;
    private final int ID_ACCOUNT=4;
    private final int ID_PROFILE=5;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME,R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION,R.drawable.ic_baseline_notifications_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT,R.drawable.ic_baseline_search_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGE,R.drawable.ic_baseline_add_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE,R.drawable.ic_baseline_person_24));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Toast.makeText(MainActivity.this,"clicked item"+ item.getId(),Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                String name;
                switch (item.getId()){

                    case ID_HOME :name = "HOME";
                    break;

                    case ID_NOTIFICATION :name = "Notification";
                        break;

                    case ID_MESSAGE :name = "Message";
                        break;

                    case ID_ACCOUNT :name = "Account";
                        break;




                    default:name="" ;
                }
            Toast.makeText(MainActivity.this,name,Toast.LENGTH_SHORT).show();

             if(item.getId() == ID_PROFILE) {
                 firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                 String Id = firebaseUser.getUid();
                 firestore.collection("Users").document(Id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot){
                         String username = documentSnapshot.get("userName").toString();
                         if(username.isEmpty()){
                             startActivity(new Intent(MainActivity.this,AddNameActivity.class));
                         } else{
                             startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                         }
                     }
                 });



             }
            }
        });










    }
}