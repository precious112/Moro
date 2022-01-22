package com.precious.moro.ViewModels;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.precious.moro.Models.Follower;
import com.precious.moro.Repository.SearchFragmentRepository;

import java.util.List;
public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<Follower>> SearchedUsers=new MutableLiveData<>();
    private SearchFragmentRepository mRepo;
    public  MutableLiveData<String> SearchName=new MutableLiveData<>();

    public void init(Context context){
        //if(SearchedUsers != null){
            //SearchedUsers.setValue(null);
        //}

        mRepo= SearchFragmentRepository.getInstance(context);
        SearchedUsers.setValue(mRepo.getUserQuery(SearchName).getValue());
    }

    public LiveData<List<Follower>> getSearchedUsers(){
        return SearchedUsers;
    }
}
