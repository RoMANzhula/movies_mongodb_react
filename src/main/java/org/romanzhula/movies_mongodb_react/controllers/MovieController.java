package org.romanzhula.movies_mongodb_react.controllers;

import org.bson.types.ObjectId;
import org.romanzhula.movies_mongodb_react.models.Movie;
import org.romanzhula.movies_mongodb_react.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.allMovies(), HttpStatus.OK);
    }

    @GetMapping("/movies/find_by_id/{id}")
    public ResponseEntity<Optional<Movie>> getMovieById(
            @PathVariable ObjectId id
    ) {
        return new ResponseEntity<>(movieService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/movies/find_by_imdbId/{imdbId}")
    public ResponseEntity<Optional<Movie>> getMovieByImdbId(
            @PathVariable String imdbId
    ) {
        return new ResponseEntity<>(movieService.findByImdbId(imdbId), HttpStatus.OK);
    }
}
