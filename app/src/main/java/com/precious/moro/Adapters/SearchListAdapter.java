package com.precious.moro.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.precious.moro.Models.Follower;
import com.precious.moro.Models.FollowerList;
import com.precious.moro.R;
import com.precious.moro.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {
    private List<Follower> followerList;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private HashMap<String,String> FL;
    Context context;

    public SearchListAdapter(Context context, List<Follower> followerList) {
        this.context = context;
        this.followerList = followerList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private CircularImageView profileImage;
        private Button follow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.image_profile);
            follow = itemView.findViewById(R.id.follow_button);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_list, parent, false);
        return new MyViewHolder(view);

    }

    public void setList(List<Follower> list){
        this.followerList.clear();
        this.followerList.addAll(list);
        notifyDataSetChanged();

    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Follower follower = followerList.get(position);
        holder.userName.setText(follower.getUserName());
        Glide.with(context).load(follower.getImageProfile()).into(holder.profileImage);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FL=follower.getFollowers();
        String nName = follower.getUserName();
        String Id = follower.getUserID();
        String ImageUri = follower.getImageProfile();
        if (FL != null) {
            if (FL.containsKey(firebaseUser.getUid())) {
                holder.follow.setText("Unfollow");
            }
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("FollowerList").child(follower.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    FollowerList G = snapshot.getValue(FollowerList.class);
                    FL=G.getFollowersList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FL==null){
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("FollowerList").child(follower.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FollowerList G = snapshot.getValue(FollowerList.class);
                            FL=G.getFollowersList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



                if (FL.containsKey(firebaseUser.getUid()) == false) {
                    FL.put(firebaseUser.getUid(), firebaseUser.getUid());
                    HashMap<String,Object> map=new HashMap<>();
                    map.put(firebaseUser.getUid(), firebaseUser.getUid());
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("FollowerList").child(follower.getUserID()).child("followersList").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            holder.follow.setText("Unfollow");
                            Follower F = new Follower(FL, Id, nName, ImageUri);
                            followerList.clear();
                            followerList.add(position, F);
                            notifyDataSetChanged();
                            //notifyItemChanged(position);

                        }
                    });

                    //mDatabase.child("FollowerList").child(follower.getUserID()).setValue(FL);

                    //notifyItemChanged(position);

                } else {
                    FL.remove(firebaseUser.getUid());
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    //HashMap<String,String> FL = follower.getFollowers();
                    //mDatabase.child("FollowerList").child(follower.getUserID()).setValue(FL);
                    mDatabase.child("FollowerList").child(follower.getUserID()).child("followersList").child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            holder.follow.setText("Follow");
                            Follower F = new Follower(FL, Id, nName, ImageUri);
                            followerList.clear();
                            followerList.add(position, F);
                            notifyDataSetChanged();

                        }
                    });



                }
                notifyItemChanged(position);

            }
        });
        holder.itemView.setTag(followerList.get(position));
    }

    @Override
    public int getItemCount() {
        return followerList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
       super.setHasStableIds(hasStableIds);
    }
}