package com.example.moviememoir.Model;
//control Watchlist Repository
import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class WatchlistViewModel extends ViewModel {
    private WatchlistRepository watchlistRepository;
    private MutableLiveData<List<Watchlist>> allWatchlist;
    public WatchlistViewModel(){
        allWatchlist = new MutableLiveData<>();
    }

    public void setWatchlist(List<Watchlist> watchlist){
        allWatchlist.setValue(watchlist);
    }

    public LiveData<List<Watchlist>> getAllWatchlist(String useridInput){
        return watchlistRepository.getAllWatchlist(useridInput);
    }
    public List<Watchlist> getWatchlistByID(String useridInput){
        return watchlistRepository.getAllWatchlistByID(useridInput);
    }

    public void initializeVars(Application application){
        watchlistRepository = new WatchlistRepository(application);
    }
    public void insert(Watchlist watchlist){
        watchlistRepository.insert(watchlist);
    }

    public void insertAll(Watchlist... watchlists){
        watchlistRepository.insertAll(watchlists);
    }
    public void deleteAll(){
        watchlistRepository.deteleAll();
    }
    public void update(Watchlist...watchlists){
        watchlistRepository.updateWatchlists(watchlists);
    }
    public Watchlist findByID(int id){
        return watchlistRepository.findByID(id);
    }
    public void deleteByID(int watchidInput){
        watchlistRepository.deleteByID(watchidInput);
    }
}
