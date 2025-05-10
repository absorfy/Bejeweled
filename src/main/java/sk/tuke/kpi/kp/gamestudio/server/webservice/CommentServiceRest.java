package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.entity.Comment;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.server.dto.CommentDTO;
import sk.tuke.kpi.kp.gamestudio.service.CommentService;
import sk.tuke.kpi.kp.gamestudio.service.PlayerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{game}")
    public List<CommentDTO> getComments(@PathVariable String game) {
        return commentService.getComments(game).stream().map(Comment::toDTO).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO) {
        Player player = playerService.getPlayer(commentDTO.getPlayerLogin());

        if (player == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Player with login '" + commentDTO.getPlayerLogin() + "' not found");
        }

        Comment comment = new Comment(
                commentDTO.getGame(),
                player,
                commentDTO.getComment(),
                commentDTO.getCommentedOn()
        );

        commentService.addComment(comment);
        messagingTemplate.convertAndSend("/topic/comments", commentDTO);
        return ResponseEntity.ok("Comment added");
    }
}
