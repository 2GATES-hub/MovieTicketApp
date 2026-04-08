package com.example.movieticket.models;

public class Ticket {
    private String id;
    private String userId;
    private String movieId;
    private String movieTitle;
    private String showtimeId;
    private String theaterName;
    private String date;
    private String time;
    private int seatCount;
    private double totalPrice;
    private String bookingTime;
    private String status; // "confirmed", "cancelled"
    private String fcmToken; // for push notification

    public Ticket() {}

    public Ticket(String userId, String movieId, String movieTitle, String showtimeId,
                  String theaterName, String date, String time,
                  int seatCount, double totalPrice, String bookingTime, String fcmToken) {
        this.userId = userId;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.showtimeId = showtimeId;
        this.theaterName = theaterName;
        this.date = date;
        this.time = time;
        this.seatCount = seatCount;
        this.totalPrice = totalPrice;
        this.bookingTime = bookingTime;
        this.status = "confirmed";
        this.fcmToken = fcmToken;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}
