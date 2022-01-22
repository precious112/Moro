package com.precious.moro.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.precious.moro.Models.Follower;
import com.precious.moro.Models.Videos;
import com.precious.moro.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.MyViewHolder> {
    private List<Videos> list;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseDatabase firebaseDatabase;
    Context context;
    public HomeFragmentAdapter(List<Videos> list, Context context){
        this.list=list;
        this.context=context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private CircularImageView ProfilePic;
        private ImageView LoveBtn;
        private VideoView Video;
        private ProgressBar pd;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username7);
            ProfilePic = itemView.findViewById(R.id.DProfile);
            LoveBtn = itemView.findViewById(R.id.imageView4);
            Video=itemView.findViewById(R.id.videoView1);
            pd=itemView.findViewById(R.id.pd);
        }
    }
    public void setList(List<Videos> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_item, parent, false);
        return new HomeFragmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Videos video= list.get(position);
       Uri uri= Uri.parse(video.getUri());
       holder.Video.setVideoPath(video.getUri());
       holder.Video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
           @Override
           public void onPrepared(MediaPlayer mp) {
               mp.start();
               holder.pd.setVisibility(View.GONE);
           }
       });
       holder.Video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
           @Override
           public void onCompletion(MediaPlayer mp) {
               mp.start();
           }
       });
       holder.Video.setVisibility(View.VISIBLE);
       DatabaseReference ref= FirebaseDatabase.getInstance().getReference("User");
       ref.child(video.getUserId()).child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               holder.userName.setText(snapshot.getValue().toString());

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       ref.child(video.getUserId()).child("imageProfile").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Glide.with(context).load(snapshot.getValue()).into(holder.ProfilePic);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
