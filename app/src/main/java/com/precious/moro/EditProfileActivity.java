package com.precious.moro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.precious.moro.Models.FollowerList;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    Button btn1;
    Button btn2;
    Button btn3;
    EditText username;
    EditText phone;
    EditText email;
    EditText DOB;
    EditText gender;
    CircularImageView ProfilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private int IMAGE_GALLERY_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btn1 = (Button) findViewById(R.id.choosebtn);
        btn2 = (Button) findViewById(R.id.uploadbtn);
        btn3 = (Button) findViewById(R.id.Editbtn);
        ProfilePic = findViewById(R.id.EditProfileActivityPic);
        username = (EditText) findViewById(R.id.editTextUsername);
        phone = (EditText) findViewById(R.id.editTextPhone);
        email = (EditText) findViewById(R.id.editTextTextEmail);
        DOB = (EditText) findViewById(R.id.editTextTextDOB);
        gender = (EditText) findViewById(R.id.editTextGender);
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                String ImageProfile = documentSnapshot.getString("imageProfile");
                Glide.with(EditProfileActivity.this).load(ImageProfile).into(ProfilePic);

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }


        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicture();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = firebaseUser.getUid();
                String userName = username.getText().toString();
                String Phone = phone.getText().toString();
                String Email = email.getText().toString();
                String dob = DOB.getText().toString();
                String Gender = gender.getText().toString();
                if (userName.isEmpty() == false) {
                    firestore.collection("Users").document(Id).update("userName", userName);
                }
                if (Phone.isEmpty() == false) {
                    firestore.collection("Users").document(Id).update("userPhone", Phone);
                }
                if (Email.isEmpty() == false) {
                    firestore.collection("Users").document(Id).update("email", Email);
                }
                if (dob.isEmpty() == false) {
                    firestore.collection("Users").document(Id).update("dateOfBirth", dob);
                }
                if (Gender.isEmpty() == false) {
                    firestore.collection("Users").document(Id).update("gender", Gender);
                }
                Toast.makeText(EditProfileActivity.this, "Changes made successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));


            }
        });


    }

    private void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), IMAGE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            imageUri = data.getData();

            //try {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //ProfilePic.setImageBitmap(bitmap);
            //} catch (Exception e) {
                //e.printStackTrace();
            //}
        }
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("uploading Image..");
        pd.show();


        final String randomKey = UUID.randomUUID().toString();


// Create a reference to 'images/mountains.jpg'
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("ImagesProfile/" + System.currentTimeMillis()+"."+getFileExtention(imageUri));
        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                final String sdownload_url = String.valueOf(downloadUrl);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("imageProfile", sdownload_url);

                pd.dismiss();
                firestore.collection("Users").document(firebaseUser.getUid()).update(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"upload successfully",Toast.LENGTH_SHORT).show();

                                getInfo();
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"upload Failed",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void getInfo() {
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String imageProfile = documentSnapshot.getString("imageProfile");
                Glide.with(EditProfileActivity.this).load(imageProfile).into(ProfilePic);
                addToRTDatabase();




            }
        });
    }

    private void addToRTDatabase(){
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String imageProfile = documentSnapshot.getString("imageProfile");
                String username= documentSnapshot.getString("userName");
                User user = new User(firebaseUser.getUid(),username,"",imageProfile,"","","");
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("User").child(firebaseUser.getUid()).setValue(user);
                FollowerList FL = new FollowerList(new HashMap<String,String>());
                HashMap<String,String> H = new HashMap<String, String>();
                H.put("s","s");
                H.put("v","v");
                //H.put(firebaseUser.getUid(),firebaseUser.getUid());
                FL.setFollowersList(H);
                mDatabase.child("FollowerList").child(firebaseUser.getUid()).setValue(FL);




            }
        });
    }
}