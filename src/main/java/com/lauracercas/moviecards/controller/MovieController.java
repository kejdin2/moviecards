package com.lauracercas.moviecards.controller;

import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.service.movie.MovieService;
import com.lauracercas.moviecards.service.client.MovieServiceClient;
import com.lauracercas.moviecards.util.Messages;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class MovieController {

    private final MovieService movieService;
    private final MovieServiceClient movieServiceClient;


    public MovieController(MovieService movieService,
                       MovieServiceClient movieServiceClient) {
    this.movieService = movieService;
    this.movieServiceClient = movieServiceClient;
}


    @GetMapping("movies")
    public String getMoviesList(Model model) {
    model.addAttribute("movies", movieServiceClient.getAllMovies());
    return "movies/list";
}


    @GetMapping("movies/new")
    public String newMovie(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("title", Messages.NEW_MOVIE_TITLE);
        return "movies/form";
    }

    @PostMapping("saveMovie")
    public String saveMovie(@ModelAttribute Movie movie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "movies/form";
        }
        Movie movieSaved = movieServiceClient.saveMovie(movie); 
        if (movieSaved.getId() != null) {
            model.addAttribute("message", Messages.UPDATED_MOVIE_SUCCESS);
        } else {
            model.addAttribute("message", Messages.SAVED_MOVIE_SUCCESS);
        }

        model.addAttribute("movie", movieSaved);
        model.addAttribute("title", Messages.EDIT_MOVIE_TITLE);
        return "movies/form";
    }

    @GetMapping("editMovie/{movieId}")
    public String editMovie(@PathVariable Integer movieId, Model model) {
        Movie movie = movieService.getMovieById(movieId);
        List<Actor> actors = movie.getActors();
        model.addAttribute("movie", movie);
        model.addAttribute("actors", actors);

        model.addAttribute("title", Messages.EDIT_MOVIE_TITLE);

        return "movies/form";
    }


}
