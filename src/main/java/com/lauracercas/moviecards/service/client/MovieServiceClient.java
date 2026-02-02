package com.lauracercas.moviecards.service.client;

import com.lauracercas.moviecards.model.Movie;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class MovieServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://127.0.0.1:8080/api/movies";

    public List<Movie> getAllMovies() {
        Movie[] movies = restTemplate.getForObject(BASE_URL, Movie[].class);
        return Arrays.asList(movies);
    }

    public Movie saveMovie(Movie movie) {
        return restTemplate.postForObject(BASE_URL, movie, Movie.class);
    }
}
