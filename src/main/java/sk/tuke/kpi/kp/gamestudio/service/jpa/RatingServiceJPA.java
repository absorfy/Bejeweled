package sk.tuke.kpi.kp.gamestudio.service.jpa;

import sk.tuke.kpi.kp.gamestudio.entity.Rating;
import sk.tuke.kpi.kp.gamestudio.service.RatingException;
import sk.tuke.kpi.kp.gamestudio.service.RatingService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            boolean isExist = (boolean) entityManager.createNamedQuery("Rating.isExist")
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer()).getSingleResult();

            if (isExist) {
                entityManager.createNamedQuery("Rating.updateRating")
                        .setParameter("rating", rating.getRating())
                        .setParameter("ratedOn", rating.getRatedOn())
                        .setParameter("game", rating.getGame())
                        .setParameter("player", rating.getPlayer())
                        .executeUpdate();
            } else {
                entityManager.persist(rating);
            }
        } catch (PersistenceException e) {
            throw new RatingException("Problem setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            return (int) Math.round((double)entityManager.createNamedQuery("Rating.getAverageRating")
                    .setParameter("game", game).getSingleResult());
        } catch (PersistenceException e) {
            throw new RatingException("Problem getting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            List<Integer> results = entityManager.createNamedQuery("Rating.getRating", Integer.class)
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getResultList();

            return results.isEmpty() ? 0 : results.get(0);
        } catch (PersistenceException e) {
            throw new RatingException("Problem getting rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try {
            entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
        } catch (PersistenceException e) {
            throw new RatingException("Problem deleting rating", e);
        }
    }
}
