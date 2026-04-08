package com.example.btcn.models;

import java.util.Date;

public class Showtime {
    private String id;
    private String movieId;
    private String theaterId;
    private long timestamp;
    private double price;

    public Showtime() {}

    public Showtime(String id, String movieId, String theaterId, long timestamp, double price) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.timestamp = timestamp;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
