package org.romanzhula.movies_mongodb_react.services;

import org.bson.types.ObjectId;
import org.romanzhula.movies_mongodb_react.models.Movie;
import org.romanzhula.movies_mongodb_react.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findById(ObjectId id) {
        return movieRepository.findById(id);
    }

    public Optional<Movie> findByImdbId(String imdbId) {
        return movieRepository.findMovieByImdbId(imdbId);
    }
}
