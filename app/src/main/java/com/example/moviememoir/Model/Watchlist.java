package com.example.moviememoir.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Watchlist {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "movieName")
    public String movieName;

    @ColumnInfo(name = "releaseDate")
    public String releaseDate;

    @ColumnInfo(name = "addingDate")
    public String addingDate;

    public Watchlist(int uid, String movieName, String releaseDate, String addingDate) {
        this.uid = uid;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.addingDate = addingDate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(String addingDate) {
        this.addingDate = addingDate;
    }

}
