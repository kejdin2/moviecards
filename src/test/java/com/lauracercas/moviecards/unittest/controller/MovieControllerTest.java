package com.lauracercas.moviecards.unittest.controller;

import com.lauracercas.moviecards.controller.MovieController;
import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.service.client.MovieServiceClient;
import com.lauracercas.moviecards.service.movie.MovieService;
import com.lauracercas.moviecards.util.Messages;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MovieControllerTest {

    private MovieController controller;
    private AutoCloseable closeable;

    @Mock MovieService movieServiceMock;
    @Mock MovieServiceClient movieClientMock;
    @Mock Model model;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
        controller = new MovieController(movieServiceMock, movieClientMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void shouldGoListMovieAndGetAllMovies() {
        when(movieClientMock.getAllMovies()).thenReturn(List.of());

        String view = controller.getMoviesList(model);

        assertEquals("movies/list", view);
        verify(model).addAttribute(eq("movies"), any());
    }

    @Test
    void shouldInitializeMovie() {
        String view = controller.newMovie(model);

        assertEquals("movies/form", view);
        verify(model).addAttribute(eq("movie"), any(Movie.class));
        verify(model).addAttribute("title", Messages.NEW_MOVIE_TITLE);
    }

    @Test
    void shouldSaveMovieWithNoErrors() {
        Movie movie = new Movie();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(movieClientMock.saveMovie(any())).thenReturn(movie);

        String view = controller.saveMovie(movie, result, model);

        assertEquals("movies/form", view);
    }

    @Test
    void shouldTrySaveMovieWithErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String view = controller.saveMovie(new Movie(), result, model);

        assertEquals("movies/form", view);
    }

    @Test
    void shouldGoToEditMovie() {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setActors(List.of(new Actor()));

        when(movieServiceMock.getMovieById(1)).thenReturn(movie);

        String view = controller.editMovie(1, model);

        assertEquals("movies/form", view);
    }
}
