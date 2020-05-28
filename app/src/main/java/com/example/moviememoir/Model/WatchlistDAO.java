package com.example.moviememoir.Model;
//control Watchlist model
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchlistDAO {
    @Query("SELECT * FROM Watchlist")
    LiveData<List<Watchlist>> getAll();

    @Query("SELECT * FROM Watchlist WHERE watchid = :watchidInput LIMIT 1")
    Watchlist findByID(int watchidInput);

    @Insert
    void insertAll(Watchlist... watchlists);
    @Insert long insert(Watchlist watchlist);

    @Delete
    void delete(Watchlist watchlist);

    @Update(onConflict = REPLACE)
    void updateWatchlist(Watchlist... watchlists);
    @Query("DELETE FROM Watchlist")
    void deleteAll();

    @Query("SELECT * FROM watchlist WHERE userid = :useridInput")
    LiveData<List<Watchlist>> findByUserid(String useridInput);

    @Query("SELECT * FROM watchlist WHERE userid = :useridInput")
    List<Watchlist> findByUseridList(String useridInput);

    @Query("DELETE FROM watchlist WHERE watchid = :watchidInput")
    void deleteByID(int watchidInput);
}
