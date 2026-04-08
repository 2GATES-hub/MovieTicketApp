package com.example.movieticket.models;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private String duration;
    private String description;
    private String posterUrl;
    private double rating;

    public Movie() {}

    public Movie(String id, String title, String genre, String duration,
                 String description, String posterUrl, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.description = description;
        this.posterUrl = posterUrl;
        this.rating = rating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
