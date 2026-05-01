package com.example.cinefast;

import java.util.List;

public class Booking {
    private String bookingId;
    private String userId;
    private String movieName;
    private String movieImage;
    private int seats;
    private double totalPrice;
    private String dateTime;
    private List<String> selectedSeatLabels;

    // Default constructor required for Firebase
    public Booking() {
    }

    public Booking(String bookingId, String userId, String movieName, String movieImage,
                   int seats, double totalPrice, String dateTime, List<String> selectedSeatLabels) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieName = movieName;
        this.movieImage = movieImage;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.selectedSeatLabels = selectedSeatLabels;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<String> getSelectedSeatLabels() {
        return selectedSeatLabels;
    }

    public void setSelectedSeatLabels(List<String> selectedSeatLabels) {
        this.selectedSeatLabels = selectedSeatLabels;
    }
}
