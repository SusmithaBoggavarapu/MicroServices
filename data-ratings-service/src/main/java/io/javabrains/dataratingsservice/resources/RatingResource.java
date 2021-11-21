package io.javabrains.dataratingsservice.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/ratings")
public class RatingResource {

    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }

    @RequestMapping("/users/{userId}")
    public UserRating getRatings(@PathVariable("userId") String userId) {

        List<Rating> ratings = new ArrayList<>();

        ratings.add(new Rating("UserID:" + userId + "Movie1" , 4));
        ratings.add(new Rating("UserID:" + userId + "Movie2" , 3));
        return new UserRating(ratings);

    }
}
