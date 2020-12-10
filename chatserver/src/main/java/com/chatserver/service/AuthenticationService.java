package com.chatserver.service;

import com.chatserver.persistence.User;
import com.chatserver.persistence.SimpleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class contains the log in logic.
 */

@Service
public class AuthenticationService {
    @Autowired
    private SimpleUserRepository simpleUserRepository;

    // logged in users.
    private Set<User> loggedInUsers = new HashSet<>();

    public boolean logIn(String username, String password){
        List<User> allUsers = simpleUserRepository.findAll();
        for(User user : allUsers){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                loggedInUsers.add(user);
                return true;
            }
        }

        return false;
    }

    public Set<User> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void logOut(String username){
        Optional<User> loggedInUser = simpleUserRepository.findAll()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
        loggedInUser.ifPresent(user -> loggedInUsers.remove(user));
    }
}
