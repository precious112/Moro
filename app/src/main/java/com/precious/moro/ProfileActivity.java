package com.precious.moro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CircularImageView ProfilePic;
    Button btn;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btn = (Button) findViewById(R.id.ProfileActivitybutton3);
        username = (TextView) findViewById(R.id.ProfileActivityusername2);
        ProfilePic= findViewById(R.id.circularImageView);
        String Id = firebaseUser.getUid();
        firestore.collection("Users").document(Id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                String Username = documentSnapshot.get("userName").toString();
                String imageProfile = documentSnapshot.getString("imageProfile");
                Glide.with(ProfileActivity.this).load(imageProfile).into(ProfilePic);
                username.setText(Username);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));


            }
        });


    }
}