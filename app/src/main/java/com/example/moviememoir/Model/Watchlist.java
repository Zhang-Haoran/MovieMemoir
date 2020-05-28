package com.example.moviememoir.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Watchlist {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "watchid")
    public int watchid;

    @ColumnInfo(name = "movieName")
    public String movieName;

    @ColumnInfo(name = "releaseDate")
    public String releaseDate;

    @ColumnInfo(name = "addDate")
    public String addDate;

    @ColumnInfo(name = "addTime")
    public String addTime;

    @ColumnInfo(name = "userid")
    public String userid;

    @ColumnInfo(name = "movieid")
    public String movieid;

    public Watchlist(String movieName, String releaseDate, String addDate, String addTime, String userid, String movieid) {
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.addDate = addDate;
        this.addTime = addTime;
        this.userid = userid;
        this.movieid = movieid;
    }

    public int getWatchid() {
        return watchid;
    }

    public void setWatchid(int watchid) {
        this.watchid = watchid;
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

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }
}
