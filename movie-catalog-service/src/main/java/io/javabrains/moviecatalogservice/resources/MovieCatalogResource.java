package io.javabrains.moviecatalogservice.resources;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import io.javabrains.moviecatalogservice.resources.model.CatalogItem;
import io.javabrains.moviecatalogservice.resources.model.Movie;
import io.javabrains.moviecatalogservice.resources.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder builder;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/{userId}")
    public String getCatalog(@PathVariable("userId") String userId) {
        List<CatalogItem> items = getCatalogFromClients(userId);
        String rawHTML = "<html>" ;
        for (CatalogItem i: items) {
            rawHTML += "<p> Movie Name:";
            rawHTML += i.getName();
            rawHTML += "</p>";
            rawHTML += "<p> Rating:";
            rawHTML += i.getRating();
            rawHTML += "</p>";
        }
        rawHTML = "</html>" ;
        return items.toString();
    }

    public List<CatalogItem> getCatalogFromClients(String userId) {

        //discoveryClient.getInstancesById();
        //get all rated movie IDs
        UserRating response = restTemplate.getForObject("http://ratings-data-service/ratings/users/" + userId, UserRating.class);
        //Arrays.asList(new Rating("ID1", 4), new Rating("ID2", 3));


        return response.getRatingList().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
           /* Movie movie = builder.build()
                    .get()
                    .uri("http://localhost:8081/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/
            return new CatalogItem(movie.getMovieId(), movie.getName(), rating.getRating());
        }).collect(Collectors.toList());
        //for each movie ID call movie info service and get details
        //return Collections.singletonList(new CatalogItem("Alice In Wonderland", "fantasy", 4));
    }

}
