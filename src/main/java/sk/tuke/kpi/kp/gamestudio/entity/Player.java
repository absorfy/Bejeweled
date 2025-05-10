package sk.tuke.kpi.kp.gamestudio.entity;

import javax.persistence.*;
import java.util.List;


@Entity
@NamedQuery( name = "Player.getPlayers",
        query = "SELECT p FROM Player p")
@NamedQuery( name = "Player.getPlayer",
        query = "SELECT p FROM Player p WHERE p.login=:login")
public class Player {
    @Id
    @GeneratedValue
    private int ident;
    @Column(unique = true)
    private String login;
    private String password;

    @OneToMany(mappedBy = "player", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "player", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Score> scores;

    @OneToMany(mappedBy = "player", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Rating> ratings;

    public Player() {}

    public Player(String login) {
        this(login, login);
    }

    public Player(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return "Player {" +
                "login='" + login + "'" +
                ", password='" + password +
                "}";
    }
}
