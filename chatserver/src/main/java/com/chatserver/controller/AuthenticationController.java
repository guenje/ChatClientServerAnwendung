package com.chatserver.controller;

import com.chatserver.persistence.User;
import com.chatserver.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Controller to handle authentication issues.
 */

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("logIn")
    public boolean logIn(@RequestParam String username, @RequestParam String password){
        return authenticationService.logIn(username, password);
    }

    @PostMapping("logOut")
    public void logOut(@RequestParam String username){
        authenticationService.logOut(username);
    }

    @GetMapping("loggedInUsers")
    public Set<User> getLoggedInUsers(){
        return authenticationService.getLoggedInUsers();
    }
}
