package com.exam.midterm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "MovieServlet", value = "/movies/*")
public class MovieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        StringBuilder html = new StringBuilder();
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");

        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                List < Movie > movies = MovieDAO.getMovies();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                html.append("<html><body><h1>List of movies:</h1><ul>");
                for (Movie movie: movies) {
                    html.append("<li>");
                    html.append("Title: ").append(movie.getTitle()).append("<br>");
                    html.append("Director: ").append(movie.getDirector()).append("<br>");
                    html.append("Date: ").append(movie.getReleaseDate().format(formatter)).append("<br>");
                    html.append("Rating: ").append(String.format("%.1f", movie.getRating())).append("<br>");
                    html.append("Rating: ").append(movie.getDescription()).append("<br>");
                    html.append(String.format("<form method='POST' action='%d'><input type='hidden' name='_method' value='DELETE'><button type='submit'>Delete</button></form>", movie.getId()));
                    html.append(String.format("<form method='GET' action='%d'><button type='submit'>Edit</button></form>", movie.getId()));

                    html.append("</li>");
                }
                html.append("</ul></body></html>");
            } catch (SQLException e) {
                throw new RuntimeException("Sql Exception: " + e.getMessage());
            }

            out.println(html);
        } else {
            int id = Integer.parseInt(pathInfo.substring(1));
            Movie movie;
            try {
                movie = MovieDAO.getMovieById(id);
                if (movie == null) {
                    out.println("<h1>Movie not found</h1>");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Sql Exception: " + e.getMessage());
            }

            html.append("<html><body><h1>Edit Movie:</h1><br><form method='POST' action=''>");
            html.append("<input type='hidden' name='_method' value='PUT'>");
            html.append(String.format("<input type='hidden' name='id' value='%d'>", movie.getId()));
            html.append("Title: <input type='text' name='title' value='").append(movie.getTitle()).append("'><br>");
            html.append("Director: <input type='text' name='director' value='").append(movie.getDirector()).append("'><br>");
            html.append("Date: <input type='date' name='releaseDate' value='").append(movie.getReleaseDate()).append("'><br>");
            html.append("Rating: <input type='number' name='rating' step='0.1' min='0' max='10' value='").append(movie.getRating()).append("'><br>");
            html.append("Description: <input type='text' name='description' value='").append(movie.getDescription()).append("'><br>");
            html.append("<input type='submit' value='Update Movie'>");
            html.append("</form></body></html>");

            out.println(html);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("DELETE".equals(req.getParameter("_method"))) {
            doDelete(req, resp);
            return;
        } else if ("PUT".equals(req.getParameter("_method"))) {
            doPut(req, resp);
            return;
        }

        resp.setContentType("text/html");

        Movie movie = Movie.of(req.getParameter("title"), req.getParameter("director"), LocalDate.parse(req.getParameter("releaseDate")),
                Float.parseFloat(req.getParameter("rating")), req.getParameter("description"));

        if (movie.getTitle().isEmpty() || movie.getDirector().isEmpty() || movie.getReleaseDate().toString().isEmpty() || String.valueOf(movie.getRating()).isEmpty() || movie.getDescription().isEmpty()) {
            try (PrintWriter out = resp.getWriter()) {
                out.println("<h1> Wrong format, fields are empty! </h1>");
            }
            return;
        }

        try {
            MovieDAO.saveMovie(movie);
        } catch (SQLException e) {
            throw new RuntimeException("Sql Exception: " + e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println("<h1> Movie was successfully saved! </h1>");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            MovieDAO.deleteMovie(id);
            try (PrintWriter out = resp.getWriter()) {
                out.println("<h1> Movie was successfully deleted! </h1>");
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getPathInfo().substring(1));
        Movie movie = Movie.of(id, req.getParameter("title"), req.getParameter("director"), LocalDate.parse(req.getParameter("releaseDate")),
                Float.parseFloat(req.getParameter("rating")), req.getParameter("description"));

        if (movie.getTitle().isEmpty() || movie.getDirector().isEmpty() || movie.getReleaseDate().toString().isEmpty() || String.valueOf(movie.getRating()).isEmpty() || movie.getDescription().isEmpty()) {
            try (PrintWriter out = resp.getWriter()) {
                out.println("<h1> Wrong format, fields are empty! </h1>");
            }
            return;
        }

        try {
            MovieDAO.updateMovie(movie);
            try (PrintWriter out = resp.getWriter()) {
                out.println("<h1> Movie was successfully updated! </h1>");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Sql Exception: " + e.getMessage());
        }
    }
}