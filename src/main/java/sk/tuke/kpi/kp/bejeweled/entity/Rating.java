package sk.tuke.kpi.kp.bejeweled.entity;


import org.hibernate.annotations.Check;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Date;


@Entity
@NamedQuery( name = "Rating.isExist",
        query = "SELECT COUNT(r) > 0 FROM Rating r WHERE r.game=:game AND r.player=:player")
@NamedQuery( name = "Rating.getAverageRating",
        query = "SELECT COALESCE(ROUND(AVG(r.rating)), 0) FROM Rating r WHERE r.game=:game")
@NamedQuery( name = "Rating.updateRating",
        query = "UPDATE Rating r SET r.rating=:rating, r.ratedOn=:ratedOn WHERE r.game=:game AND r.player=:player")
@NamedQuery( name = "Rating.resetRatings",
        query = "DELETE FROM Rating")
@NamedQuery( name = "Rating.getRating",
        query = "SELECT r.rating FROM Rating r WHERE r.game=:game AND r.player=:player")
@Check(constraints = "rating >= 1 AND rating <= 5")
public class Rating {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;

    private int rating;
    private Date ratedOn;

    public Rating() {}

    public Rating(String game, String player, int rating, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rating=" + rating +
                ", ratedOn=" + ratedOn +
                '}';
    }
}
