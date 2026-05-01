package com.example.cinefast;

public class Movie {
    private String name;
    private String genre;
    private String duration;
    private int posterResId;
    private String trailerUrl;
    private boolean isNowShowing;
    private String date;

    public Movie(String name, String genre, String duration, int posterResId, String trailerUrl, boolean isNowShowing, String date) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPosterResId() {
        return posterResId;
    }

    public void setPosterResId(int posterResId) {
        this.posterResId = posterResId;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public boolean isNowShowing() {
        return isNowShowing;
    }

    public void setNowShowing(boolean nowShowing) {
        isNowShowing = nowShowing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
