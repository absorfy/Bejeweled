package sk.tuke.kpi.kp.gamestudio.service.jpa;


import sk.tuke.kpi.kp.gamestudio.entity.Score;
import sk.tuke.kpi.kp.gamestudio.service.CommentException;
import sk.tuke.kpi.kp.gamestudio.service.ScoreException;
import sk.tuke.kpi.kp.gamestudio.service.ScoreService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;


@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        try {
            entityManager.persist(score);
        } catch (PersistenceException e) {
            throw new CommentException("Problem inserting score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        try {
            return (List<Score>) entityManager.createNamedQuery("Score.getTopScores")
                    .setParameter("game", game).setMaxResults(10).getResultList();
        } catch (PersistenceException e) {
            throw new CommentException("Problem getting top score", e);
        }
    }

    @Override
    public void reset() throws ScoreException {
        try {
            entityManager.createNamedQuery("Score.resetScores").executeUpdate();
        } catch (PersistenceException e) {
            throw new CommentException("Problem deleting scores", e);
        }
    }
}
