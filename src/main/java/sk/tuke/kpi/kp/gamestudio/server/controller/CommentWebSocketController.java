package sk.tuke.kpi.kp.gamestudio.server.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import sk.tuke.kpi.kp.gamestudio.server.dto.CommentDTO;

@Controller
public class CommentWebSocketController {

    @MessageMapping("/comments")
    @SendTo("/topic/comments")
    public CommentDTO broadcastComment(CommentDTO commentDTO) {
        return commentDTO;
    }
}

