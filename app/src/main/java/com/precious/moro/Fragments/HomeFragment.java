package com.precious.moro.Fragments;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.precious.moro.Adapters.HomeFragmentAdapter;
import com.precious.moro.Interfaces.VideosCallBack;
import com.precious.moro.Models.Videos;
import com.precious.moro.R;
import com.precious.moro.ViewModels.HomeActivityViewModel;
import com.precious.moro.databinding.FragmentHomeBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements VideosCallBack {
    private FragmentHomeBinding binding;
    private HomeActivityViewModel homeActivityViewModel;
    private HomeFragmentAdapter homeFragmentAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home,container1,false);
        binding.setLifecycleOwner(this);
        homeActivityViewModel=  ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        homeActivityViewModel.init(getActivity());
        homeFragmentAdapter=new HomeFragmentAdapter(homeActivityViewModel.getVideos().getValue(),getContext());


        //binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));


        binding.recyclerView2.setAdapter(homeFragmentAdapter);

        //homeFragmentAdapter.notifyDataSetChanged();
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        homeActivityViewModel.getVideos().removeObservers(getViewLifecycleOwner());
        super.onDestroyView();
    }


    @Override
    public void videoCallBacks() {
        if(getView() != null){
            homeActivityViewModel.getVideos().observe(getViewLifecycleOwner(), new Observer<List<Videos>>() {
                @Override
                public void onChanged(@Nullable List<Videos> videos) {
                    homeFragmentAdapter.notifyDataSetChanged();
                }
            });
        }

    }
}