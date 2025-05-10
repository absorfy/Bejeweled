package sk.tuke.kpi.kp.gamestudio.service;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sk.tuke.kpi.kp.gamestudio.entity.Comment;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.TestConfig;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PlayerService playerService;

    @Test
    void reset() {
        commentService.reset();
        assertEquals(0, commentService.getComments("bejeweled").size());
    }

    @Test
    void addComment() {
        playerService.reset();
        commentService.reset();

        Player player = new Player("jaro");
        playerService.addPlayer(player);
        commentService.addComment(new Comment("bejeweled", player, "Super game!", new Date()));
        var comments = commentService.getComments("bejeweled");
        assertEquals(1, comments.size());
        var comment = comments.get(0);
        assertEquals("jaro", comment.getPlayer().getLogin());
        assertEquals("bejeweled", comment.getGame());
        assertEquals("Super game!", comment.getComment());
    }

    @Test
    void getComments() {
        playerService.reset();
        commentService.reset();

        Player player = new Player("jaro");
        playerService.addPlayer(player);
        commentService.addComment(new Comment("bejeweled", player, "Hello, my name is jaro", new Date()));
        player = new Player("fero");
        playerService.addPlayer(player);
        commentService.addComment(new Comment("mines", player, "the game is difficult", new Date()));
        player = new Player("mara");
        playerService.addPlayer(player);
        commentService.addComment(new Comment("bejeweled", player, "super", new Date()));
        player = new Player("jano");
        playerService.addPlayer(player);
        commentService.addComment(new Comment("bejeweled", player, "add more activities", new Date()));
        player = new Player("zuza");
        playerService.addPlayer(player);
        commentService.addComment(new Comment("bejeweled", player, "wow", new Date()));
        var comments =  commentService.getComments("bejeweled");
        assertEquals(4, comments.size());

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().getLogin().equals("jaro") &&
                        comm.getComment().equals("Hello, my name is jaro")
        ));

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().getLogin().equals("mara") &&
                        comm.getComment().equals("super")
        ));

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().getLogin().equals("jano") &&
                        comm.getComment().equals("add more activities")
        ));

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().getLogin().equals("zuza") &&
                        comm.getComment().equals("wow")
        ));
    }


}