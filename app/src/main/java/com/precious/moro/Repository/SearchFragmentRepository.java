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
import com.google.firebase.firestore.FirebaseFirestore;
import com.precious.moro.Adapters.SearchListAdapter;
import com.precious.moro.Interfaces.SearchCallBack;
import com.precious.moro.Interfaces.VideosCallBack;
import com.precious.moro.Models.Follower;
import com.precious.moro.Models.FollowerList;
import com.precious.moro.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragmentRepository {

    private static SearchFragmentRepository instance;
    //userQuery takes all search results of the input of the user
    private List<Follower> userQuery=new ArrayList<>();
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private User user;
    private List<User> users=new ArrayList<>();
    private FollowerList FL;

    static Context mcontext;
    static SearchCallBack mSearchCallBack;
    public static SearchFragmentRepository getInstance(Context context){
        mcontext=context;
        if(instance == null){
            instance = new SearchFragmentRepository();
        }
        mSearchCallBack= (SearchCallBack) mcontext;
        return instance;

    }

    public MutableLiveData<List<Follower>> getUserQuery(MutableLiveData<String> name){
        getQuery(name);
        MutableLiveData<List<Follower>> data = new MutableLiveData<>();
        data.setValue(userQuery);
        return data;
    }

    private void getQuery(MutableLiveData<String> name){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        Query query= reference.child("User").orderByChild("userName").equalTo(name.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userQuery.clear();

                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    user = Snapshot.getValue(User.class);
                    reference.child("FollowerList").child(user.getUserID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FL = snapshot.getValue(FollowerList.class);
                            Follower F = new Follower(FL.getFollowersList(), user.getUserID(), user.getUserName(), user.getImageProfile());
                            userQuery.add(F);
                            mSearchCallBack.searchCallBacks();
                        }




                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                mSearchCallBack.searchCallBacks();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private  List<Follower> refresh(){
        return userQuery;
    }
}