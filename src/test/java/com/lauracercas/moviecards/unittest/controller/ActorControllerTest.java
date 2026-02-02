package com.lauracercas.moviecards.unittest.controller;

import com.lauracercas.moviecards.controller.ActorController;
import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.service.actor.ActorService;
import com.lauracercas.moviecards.service.client.ActorServiceClient;
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

class ActorControllerTest {

    private ActorController controller;
    private AutoCloseable closeable;

    @Mock ActorService actorServiceMock;              // kept for constructor
    @Mock ActorServiceClient actorClientMock;         // NEW
    @Mock Model model;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
        controller = new ActorController(actorServiceMock, actorClientMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void shouldGoListActorAndGetAllActors() {
        when(actorClientMock.getAllActors()).thenReturn(List.of());

        String view = controller.getActorsList(model);

        assertEquals("actors/list", view);
        verify(model).addAttribute(eq("actors"), any());
    }

    @Test
    void shouldInitializeActor() {
        String view = controller.newActor(model);

        assertEquals("actors/form", view);
        verify(model).addAttribute(eq("actor"), any(Actor.class));
        verify(model).addAttribute("title", Messages.NEW_ACTOR_TITLE);
    }

    @Test
    void shouldSaveActorWithNoErrors() {
        Actor actor = new Actor();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(actorClientMock.saveActor(any())).thenReturn(actor);

        String view = controller.saveActor(actor, result, model);

        assertEquals("actors/form", view);
    }

    @Test
    void shouldTrySaveActorWithErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String view = controller.saveActor(new Actor(), result, model);

        assertEquals("actors/form", view);
    }

    @Test
    void shouldGoToEditActor() {
        Actor actor = new Actor();
        actor.setId(1);
        actor.setMovies(List.of(new Movie()));

        when(actorClientMock.getActorById(1)).thenReturn(actor);

        String view = controller.editActor(1, model);

        assertEquals("actors/form", view);
    }
}
