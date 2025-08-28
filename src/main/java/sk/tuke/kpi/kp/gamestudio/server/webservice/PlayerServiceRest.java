package sk.tuke.kpi.kp.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.gamestudio.entity.Player;
import sk.tuke.kpi.kp.gamestudio.server.dto.PlayerDTO;
import sk.tuke.kpi.kp.gamestudio.service.PlayerService;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/player")
public class PlayerServiceRest {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> loginPlayer(@RequestBody PlayerDTO playerDTO, HttpSession session) {
        if(playerDTO.getLogin() == null || playerDTO.getPassword() == null || playerDTO.getLogin().length() < 3 || playerDTO.getPassword().length() < 3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid login or password");
        }

        Player player = playerService.getPlayer(playerDTO.getLogin());

        if (player == null) {
            Player newPlayer = new Player(playerDTO.getLogin(), playerDTO.getPassword());
            playerService.addPlayer(newPlayer);
            saveLoginIsSession(session, newPlayer.getLogin());
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
        }
        if (!passwordEncoder.matches(playerDTO.getPassword(), player.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
        }
        if (session.getServletContext().getAttribute(playerDTO.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Login already in use");
        }

        saveLoginIsSession(session, playerDTO.getLogin());
        return ResponseEntity.ok("Authorization successful");
    }

    private static void saveLoginIsSession(HttpSession session, String playerLogin) {
        session.setAttribute("playerLogin", playerLogin);
        session.getServletContext().setAttribute(playerLogin, session.getId());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        String login = (String) session.getAttribute("playerLogin");
        if (login != null) {
            session.getServletContext().removeAttribute(login);
            session.removeAttribute("playerLogin");
        }
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/status")
    public ResponseEntity<?> getLoginStatus(HttpSession session) {
        String login = (String) session.getAttribute("playerLogin");
        if(login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        return ResponseEntity.ok(login);
    }

}
