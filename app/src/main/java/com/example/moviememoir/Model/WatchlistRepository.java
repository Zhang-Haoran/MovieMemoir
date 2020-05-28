package com.example.moviememoir.Model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WatchlistRepository {
    private WatchlistDAO dao;
    private LiveData<List<Watchlist>> allWatchlist;
    private Watchlist watchlist;
    private List<Watchlist> watchlists;

    public WatchlistRepository(Application application){
        WatchlistDatabase db = WatchlistDatabase.getInstance(application);
        dao = db.watchlistDAO();
    }

    public LiveData<List<Watchlist>> getAllWatchlist(){
        allWatchlist = dao.getAll();
        return allWatchlist;
    }

    public void insert(final Watchlist watchlist){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(watchlist);
            }
        });
    }

    public void deteleAll(){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void detele(final Watchlist watchlist){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(watchlist);
            }
        });
    }

    public void insertAll(final Watchlist... watchlists){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(watchlists);
            }
        });
    }

    public void updateWatchlists(final Watchlist... watchlists){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateWatchlist(watchlists);
            }
        });
    }

    public Watchlist findByID(final int watchlistID){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Watchlist runWatchlist = dao.findByID(watchlistID);
                setWatchlist(runWatchlist);
            }
        });
        return watchlist;
    }

    private void setWatchlist(Watchlist Watchlist) {
        this.watchlist = watchlist;
    }

    public void deleteByID(final int watchidInput){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteByID(watchidInput);
            }
        });
    }

    public LiveData<List<Watchlist>> getAllWatchlist(String useridInput){
        allWatchlist = dao.findByUserid(useridInput);
        return allWatchlist;
    }

    public List<Watchlist> getAllWatchlistByID(String useridInput){
        watchlists = dao.findByUseridList(useridInput);
        return watchlists;
    }


}
