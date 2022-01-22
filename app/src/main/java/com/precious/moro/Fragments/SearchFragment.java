package com.precious.moro.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.precious.moro.MainActivity;
import com.precious.moro.Models.Follower;
import com.precious.moro.Models.FollowerList;
import com.precious.moro.R;
import com.precious.moro.SplashScreenActivity;
import com.precious.moro.User;
import com.precious.moro.databinding.FragmentSearchBinding;
import com.precious.moro.ViewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchCallBack {
    private FragmentSearchBinding binding;
    private FirebaseUser firebaseUser;
    private SearchListAdapter searchListAdapter;
    private SearchViewModel searchViewModel;
    private List<Follower> dummyList= new ArrayList<>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false);

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false);



        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        searchListAdapter= new SearchListAdapter(getContext(),dummyList);


        binding.recyclerView.setAdapter(searchListAdapter);



        searchViewModel= new ViewModelProvider(this).get(SearchViewModel.class);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.searchUser.getText().toString();
                //showUsers(name);
                searchViewModel.SearchName.setValue(name);
                searchViewModel.init(getActivity());

                int n=searchViewModel.getSearchedUsers().getValue().size();

                Log.d("my tag", String.valueOf(n));
                searchListAdapter.setList(searchViewModel.getSearchedUsers().getValue());


                //searchListAdapter= new SearchListAdapter(getContext(),searchViewModel.getSearchedUsers().getValue());



            }
                });



        return binding.getRoot();

    }

    @Override
    public void searchCallBacks() {
        searchViewModel.getSearchedUsers().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<Follower>>() {
            @Override
            public void onChanged(@Nullable final List<Follower> followerList) {
                int n=searchViewModel.getSearchedUsers().getValue().size();

                Log.d("my tag", String.valueOf(n));
                searchListAdapter.setList(followerList);
                //searchListAdapter.notifyDataSetChanged();



            }
        });
    }
}