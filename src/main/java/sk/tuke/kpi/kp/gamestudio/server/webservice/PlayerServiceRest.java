package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.server.dto.PlayerDTO;
import sk.tuke.kpi.kp.gamestudio.service.PlayerService;


@RestController
@RequestMapping("/api/player")
public class PlayerServiceRest {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> loginPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerService.getPlayer(playerDTO.getLogin());

        if (player == null) {
            playerService.addPlayer(new Player(playerDTO.getLogin(), playerDTO.getPassword()));
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
        }

        if (!passwordEncoder.matches(playerDTO.getPassword(), player.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
        }

        return ResponseEntity.ok("Authorization successful");
    }

}
