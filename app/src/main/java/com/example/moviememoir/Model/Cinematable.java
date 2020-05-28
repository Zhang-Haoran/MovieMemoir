package com.example.moviememoir.Model;

public class Cinematable {

    private Integer cinemaid;

    private String cinemaname;

    private String suburb;

    private String postcode;

    public Cinematable(Integer cinemaid, String cinemaname, String suburb, String postcode) {
        this.cinemaid = cinemaid;
        this.cinemaname = cinemaname;
        this.suburb = suburb;
        this.postcode = postcode;
    }

    public Cinematable() {
    }

    public Cinematable(String cinemaname, String suburb) {
        this.cinemaname = cinemaname;
        this.suburb = suburb;
    }

    public Cinematable(String cinemaid) {
    }

    public Integer getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Integer cinemaid) {
        this.cinemaid = cinemaid;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
