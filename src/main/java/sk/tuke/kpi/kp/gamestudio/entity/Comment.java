package sk.tuke.kpi.kp.gamestudio.entity;

import sk.tuke.kpi.kp.gamestudio.server.dto.CommentDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQuery( name = "Comment.getComments",
        query = "SELECT c FROM Comment c WHERE c.game=:game")
@NamedQuery( name = "Comment.resetComments",
        query = "DELETE FROM Comment")
public class Comment {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    @ManyToOne
    private Player player;
    private String comment;
    private Date commentedOn;

    public Comment() {}

    public Comment(String game, Player player, String comment, Date commentedOn) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public int getIdent() {
        return ident;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", comment='" + comment + '\'' +
                ", commentedOn=" + commentedOn +
                '}';
    }

    public CommentDTO toDTO() {
        CommentDTO dto = new CommentDTO();
        dto.setGame(game);
        dto.setComment(comment);
        dto.setCommentedOn(commentedOn);
        dto.setPlayerLogin(player.getLogin());
        return dto;
    }
}
