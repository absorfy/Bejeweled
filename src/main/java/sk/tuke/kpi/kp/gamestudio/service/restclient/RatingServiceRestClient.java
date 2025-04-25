package sk.tuke.kpi.kp.gamestudio.service.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.gamestudio.entity.Rating;
import sk.tuke.kpi.kp.gamestudio.service.RatingException;
import sk.tuke.kpi.kp.gamestudio.service.RatingService;


@Service
public class RatingServiceRestClient implements RatingService {
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForObject(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String gameName) throws RatingException {
        return restTemplate.getForObject(url+"/"+gameName, int.class);
    }

    @Override
    public int getRating(String gameName, String playerName) throws RatingException {
        return restTemplate.getForObject(url+"/"+gameName+"/"+playerName, int.class);
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
