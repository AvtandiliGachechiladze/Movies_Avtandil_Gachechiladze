package com.exam.midterm;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String INSERT_MOVIE_QUERY = "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_MOVIES_QUERY = "SELECT * FROM %s";
    private static final String SELECT_MOVIE_QUERY = "SELECT * FROM %s WHERE MovieID = %d";
    private static final String DELETE_MOVIE_QUERY = "DELETE FROM %s WHERE MovieID = %d";
    private static final String UPDATE_MOVIE_QUERY = "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE MovieID = ?";
    private static final String TABLE_MOVIES = "Movie";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_DIRECTOR = "Director";
    private static final String COLUMN_RELEASE_DATE = "ReleaseDate";
    private static final String COLUMN_RATING = "Rating";
    private static final String COLUMN_DESCRIPTION = "Description";

    public static void saveMovie(Movie movie) throws SQLException {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = prepareMovieInsertStatement(connection, movie.getTitle(), movie.getDirector(), movie.getReleaseDate(), movie.getRating(), movie.getDescription())) {
            statement.executeUpdate();
        }
    }

    public static List < Movie > getMovies() throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = getSelectPreparedStatement(conn, TABLE_MOVIES);
        ResultSet results = statement.executeQuery();
        List < Movie > movies = new ArrayList < > ();

        while (results.next()) {
            int id = results.getInt("MovieID");
            String title = results.getString("Title");
            String director = results.getString("Director");
            LocalDate releaseDate = LocalDate.parse(results.getString("ReleaseDate"), DATE_FORMAT);
            float rating = results.getFloat("Rating");
            String description = results.getString("Description");

            movies.add(Movie.of(id, title, director, releaseDate, rating, description));
        }

        results.close();
        statement.close();
        conn.close();

        return movies;
    }

    public static Movie getMovieById(int movieId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = getMoviePreparedStatement(conn, TABLE_MOVIES, movieId);
        ResultSet results = statement.executeQuery();
        List < Movie > movies = new ArrayList < > ();

        while (results.next()) {
            int id = results.getInt("MovieID");
            String title = results.getString("Title");
            String director = results.getString("Director");
            LocalDate releaseDate = LocalDate.parse(results.getString("ReleaseDate"), DATE_FORMAT);
            float rating = results.getFloat("Rating");
            String description = results.getString("Description");

            movies.add(Movie.of(id, title, director, releaseDate, rating, description));
        }

        results.close();
        statement.close();
        conn.close();

        return movies.get(0);
    }

    public static void deleteMovie(int id) throws SQLException {

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = getDeletePreparedStatement(conn, TABLE_MOVIES, id)) {

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted != 1) {
                throw new SQLException("Error deleting movie with ID " + id);
            }

        }
    }

    public static void updateMovie(Movie movie) throws SQLException {

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = getUpdatePreparedStatement(conn, TABLE_MOVIES, movie)) {

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted != 1) {
                throw new SQLException("Error updating movie with ID " + movie.getId());
            }

        }
    }

    public static PreparedStatement getSelectPreparedStatement(Connection conn, String tableName) throws SQLException {
        String query = String.format(SELECT_MOVIES_QUERY, tableName);
        return conn.prepareStatement(query);
    }

    private static PreparedStatement prepareMovieInsertStatement(Connection connection, String title, String director, LocalDate releaseDate, float rating, String description) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(String.format(INSERT_MOVIE_QUERY, TABLE_MOVIES, COLUMN_TITLE, COLUMN_DIRECTOR, COLUMN_RELEASE_DATE, COLUMN_RATING, COLUMN_DESCRIPTION));
        statement.setString(1, title);
        statement.setString(2, director);
        statement.setString(3, releaseDate.toString());
        statement.setFloat(4, rating);
        statement.setString(5, description);
        return statement;
    }

    public static PreparedStatement getDeletePreparedStatement(Connection conn, String table, int movieId) throws SQLException {
        String query = String.format(DELETE_MOVIE_QUERY, table, movieId);
        return conn.prepareStatement(query);
    }

    public static PreparedStatement getMoviePreparedStatement(Connection conn, String table, int movieId) throws SQLException {
        String query = String.format(SELECT_MOVIE_QUERY, table, movieId);
        return conn.prepareStatement(query);
    }

    public static PreparedStatement getUpdatePreparedStatement(Connection conn, String table, Movie movie) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(String.format(UPDATE_MOVIE_QUERY, table, COLUMN_TITLE, COLUMN_DIRECTOR, COLUMN_RELEASE_DATE, COLUMN_RATING, COLUMN_DESCRIPTION));
        statement.setString(1, movie.getTitle());
        statement.setString(2, movie.getDirector());
        statement.setString(3, movie.getReleaseDate().toString());
        statement.setFloat(4, movie.getRating());
        statement.setString(5, movie.getDescription());
        statement.setInt(6, movie.getId());
        return statement;
    }
}