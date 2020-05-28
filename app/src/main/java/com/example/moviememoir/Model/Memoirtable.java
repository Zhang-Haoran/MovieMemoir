package com.example.moviememoir.Model;

public class Memoirtable {

    private Integer memoirid;

    private String moviename;

    private String moviereleasedate;

    private String watchdate;

    private String watchtime;

    private String comment;

    private int ratingscore;

    private Cinematable cinemaid;
    private Usertable userid;

    public Memoirtable() {
    }



    public Integer getMemoirid() {
        return memoirid;
    }

    public void setMemoirid(Integer memoirid) {
        this.memoirid = memoirid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getMoviereleasedate() {
        return moviereleasedate;
    }

    public void setMoviereleasedate(String moviereleasedate) {
        this.moviereleasedate = moviereleasedate;
    }

    public String getWatchdate() {
        return watchdate;
    }

    public void setWatchdate(String watchdate) {
        this.watchdate = watchdate;
    }

    public String getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(String watchtime) {
        this.watchtime = watchtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRatingscore() {
        return ratingscore;
    }

    public void setRatingscore(int ratingscore) {
        this.ratingscore = ratingscore;
    }

    public Cinematable getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinematable cinemaid) {
        this.cinemaid = cinemaid;
    }

    public Usertable getUserid() {
        return userid;
    }

    public void setUserid(Usertable userid) {
        this.userid = userid;
    }

    public Memoirtable(Integer memoirid, String moviename, String moviereleasedate, String watchdate, String watchtime, String comment, int ratingscore, Cinematable cinemaid, Usertable userid) {
        this.memoirid = memoirid;
        this.moviename = moviename;
        this.moviereleasedate = moviereleasedate;
        this.watchdate = watchdate;
        this.watchtime = watchtime;
        this.comment = comment;
        this.ratingscore = ratingscore;
        this.cinemaid = cinemaid;
        this.userid = userid;
    }
}
