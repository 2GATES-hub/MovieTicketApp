package com.example.movieticket.models;

public class Showtime {
    private String id;
    private String movieId;
    private String theaterId;
    private String theaterName;
    private String date;
    private String time;
    private int availableSeats;
    private double price;

    public Showtime() {}

    public Showtime(String id, String movieId, String theaterId, String theaterName,
                    String date, String time, int availableSeats, double price) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.date = date;
        this.time = time;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
