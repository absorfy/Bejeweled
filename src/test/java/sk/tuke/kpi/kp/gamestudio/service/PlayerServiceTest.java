package sk.tuke.kpi.kp.gamestudio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import sk.tuke.kpi.kp.gamestudio.TestConfig;
import sk.tuke.kpi.kp.gamestudio.entity.Player;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class PlayerServiceTest {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void reset() {
        playerService.reset();
        assertEquals(0, playerService.getPlayers().size());
    }

    @Test
    void addPlayer() {
        playerService.reset();
        playerService.addPlayer(new Player("admin", "admin"));
        var players = playerService.getPlayers();
        assertEquals(1, players.size());
        var player = players.get(0);
        assertEquals("admin", player.getLogin());
        assertTrue(passwordEncoder.matches("admin", player.getPassword()));
    }

    @Test
    void getPassword() {
        playerService.reset();
        String storedPassword = playerService.getPlayer("admin").getPassword();
        assertNull(storedPassword);
        playerService.addPlayer(new Player("admin", "admin"));
        storedPassword = playerService.getPlayer("admin").getPassword();
        assertTrue(passwordEncoder.matches("admin", storedPassword));
        assertNull(playerService.getPlayer("jano"));
    }
}
