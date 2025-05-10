package sk.tuke.kpi.kp.gamestudio.service;

import sk.tuke.kpi.kp.gamestudio.entity.Player;

import java.util.List;

public interface PlayerService {
    void addPlayer(Player player) throws PlayerException;
    List<Player> getPlayers() throws PlayerException;
    void reset() throws PlayerException;
    Player getPlayer(String login);
}
