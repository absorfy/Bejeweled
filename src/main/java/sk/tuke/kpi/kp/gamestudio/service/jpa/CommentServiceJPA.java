package sk.tuke.kpi.kp.gamestudio.service.jpa;

import sk.tuke.kpi.kp.gamestudio.entity.Comment;
import sk.tuke.kpi.kp.gamestudio.service.CommentException;
import sk.tuke.kpi.kp.gamestudio.service.CommentService;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        try {
            entityManager.persist(comment);
        } catch (PersistenceException e) {
            throw new CommentException("Problem inserting comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try {
            return entityManager.createNamedQuery("Comment.getComments")
                    .setParameter("game", game).getResultList();
        } catch (PersistenceException e) {
            throw new CommentException("Problem getting comment", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        try {
            entityManager.createNamedQuery("Comment.resetComments").executeUpdate();
        } catch (PersistenceException e) {
            throw new CommentException("Problem deleting comments", e);
        }
    }
}
