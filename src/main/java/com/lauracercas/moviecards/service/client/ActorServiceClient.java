package com.lauracercas.moviecards.service.client;

import com.lauracercas.moviecards.model.Actor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ActorServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL =
            "http://127.0.0.1:8080/api/actors";

    public List<Actor> getAllActors() {
        Actor[] actors = restTemplate.getForObject(BASE_URL, Actor[].class);
        return Arrays.asList(actors);
    }

    public Actor saveActor(Actor actor) {
        return restTemplate.postForObject(BASE_URL, actor, Actor.class);
    }

    public Actor getActorById(Integer id) {
        return restTemplate.getForObject(BASE_URL + "/" + id, Actor.class);
    }
}
