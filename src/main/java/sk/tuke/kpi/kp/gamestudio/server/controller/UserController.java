package sk.tuke.kpi.kp.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.gamestudio.entity.Player;

//@Controller
//@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private Player loggedPlayer;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(String login, String password) {
        if("heslo".equals(password)) {
            loggedPlayer = new Player(login, "heslo");
        }

        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedPlayer = null;
        return "redirect:/";
    }

    public Player getLoggedUser() {
        return loggedPlayer;
    }

    public boolean isLogged() {
        return loggedPlayer != null;
    }
}
