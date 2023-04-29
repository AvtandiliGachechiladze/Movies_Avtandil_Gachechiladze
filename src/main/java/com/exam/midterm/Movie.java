package com.exam.midterm;

import java.time.LocalDate;

public class Movie {
    private int id;
    private final String title;
    private final String director;
    private final LocalDate releaseDate;
    private final float rating;
    private final String description;

    private Movie(int id, String title, String director, LocalDate releaseDate, float rating, String description) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
    }

    private Movie(String title, String director, LocalDate releaseDate, float rating, String description) {
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
    }

    public static Movie of(int id, String title, String director, LocalDate releaseDate, float rating, String description){
        return new Movie(id, title, director, releaseDate, rating, description);
    }

    public static Movie of(String title, String director, LocalDate releaseDate, float rating, String description){
        return new Movie(title, director, releaseDate, rating, description);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
