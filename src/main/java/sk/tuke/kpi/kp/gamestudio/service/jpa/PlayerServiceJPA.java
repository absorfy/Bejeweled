package sk.tuke.kpi.kp.gamestudio.service.jpa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.service.PlayerException;
import sk.tuke.kpi.kp.gamestudio.service.PlayerService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PlayerServiceJPA  implements PlayerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void addPlayer(Player player) throws PlayerException {
        try {
            String encodedPassword = passwordEncoder.encode(player.getPassword());
            player.setPassword(encodedPassword);
            entityManager.persist(player);
        } catch (PersistenceException e) {
            throw new PlayerException("Problem inserting player", e);
        }
    }

    @Override
    public List<Player> getPlayers() throws PlayerException {
        try {
            return entityManager.createNamedQuery("Player.getPlayers")
                    .getResultList();
        } catch (PersistenceException e) {
            throw new PlayerException("Problem getting players", e);
        }
    }

    @Override
    public void reset() throws PlayerException {
        try {
            List<Player> players = entityManager
                    .createQuery("SELECT p FROM Player p", Player.class)
                    .getResultList();
            for (Player player : players) {
                entityManager.remove(player);
            }
        } catch (PersistenceException e) {
            throw new PlayerException("Problem deleting players", e);
        }
    }

    @Override
    public Player getPlayer(String login) throws PlayerException {
        try {
            List<Player> players = entityManager.createNamedQuery("Player.getPlayer")
                    .setParameter("login", login)
                    .getResultList();

            if(players.isEmpty()) return null;
            return players.get(0);
        } catch (PersistenceException e) {
            throw new PlayerException("Problem getting password", e);
        }
    }
}
