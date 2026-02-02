package com.lauracercas.moviecards.controller;

import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.service.actor.ActorService;
import com.lauracercas.moviecards.service.client.ActorServiceClient;
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
public class ActorController {

    private final ActorService actorService; // keep for tests
    private final ActorServiceClient actorServiceClient;

    public ActorController(ActorService actorService,
                           ActorServiceClient actorServiceClient) {
        this.actorService = actorService;
        this.actorServiceClient = actorServiceClient;
    }

    @GetMapping("actors")
    public String getActorsList(Model model) {
        model.addAttribute("actors", actorServiceClient.getAllActors());
        return "actors/list";
    }

    @GetMapping("actors/new")
    public String newActor(Model model) {
        model.addAttribute("actor", new Actor());
        model.addAttribute("title", Messages.NEW_ACTOR_TITLE);
        return "actors/form";
    }

    @PostMapping("saveActor")
    public String saveActor(@ModelAttribute Actor actor,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            return "actors/form";
        }

        Actor actorSaved = actorServiceClient.saveActor(actor);

        if (actor.getId() != null) {
            model.addAttribute("message", Messages.UPDATED_ACTOR_SUCCESS);
        } else {
            model.addAttribute("message", Messages.SAVED_ACTOR_SUCCESS);
        }

        model.addAttribute("actor", actorSaved);
        model.addAttribute("title", Messages.EDIT_ACTOR_TITLE);
        return "actors/form";
    }

    @GetMapping("editActor/{actorId}")
    public String editActor(@PathVariable Integer actorId, Model model) {

        Actor actor = actorServiceClient.getActorById(actorId);
        List<Movie> movies = actor.getMovies();

        model.addAttribute("actor", actor);
        model.addAttribute("movies", movies);
        model.addAttribute("title", Messages.EDIT_ACTOR_TITLE);

        return "actors/form";
    }
}
