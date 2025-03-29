package sk.tuke.kpi.kp.bejeweled.service;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.kpi.kp.bejeweled.entity.Comment;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    void reset() {
        commentService.reset();
        assertEquals(0, commentService.getComments("bejeweled").size());
    }

    @Test
    void addComment() {
        commentService.reset();
        commentService.addComment(new Comment("bejeweled", "jaro", "Super game!", new Date()));
        var comments =  commentService.getComments("bejeweled");
        assertEquals(1, comments.size());
        var comment = comments.get(0);
        assertEquals("jaro", comment.getPlayer());
        assertEquals("bejeweled", comment.getGame());
        assertEquals("Super game!", comment.getComment());
    }

    @Test
    void getComments() {
        commentService.reset();
        commentService.addComment(new Comment("bejeweled", "jaro", "Hello, my name is jaro", new Date()));
        commentService.addComment(new Comment("mines", "fero", "the game is difficult", new Date()));
        commentService.addComment(new Comment("bejeweled", "mara", "super", new Date()));
        commentService.addComment(new Comment("bejeweled", "jano", "add more activities", new Date()));
        commentService.addComment(new Comment("bejeweled", "zuza", "wow", new Date()));
        var comments =  commentService.getComments("bejeweled");
        assertEquals(4, comments.size());

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().equals("jaro") &&
                        comm.getComment().equals("Hello, my name is jaro")
        ));

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().equals("mara") &&
                        comm.getComment().equals("super")
        ));

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().equals("jano") &&
                        comm.getComment().equals("add more activities")
        ));

        assertTrue(comments.stream().anyMatch(comm ->
                comm.getGame().equals("bejeweled") &&
                        comm.getPlayer().equals("zuza") &&
                        comm.getComment().equals("wow")
        ));
    }


}