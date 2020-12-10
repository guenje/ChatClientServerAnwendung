package com.chatclient.service;

import com.chatclient.ui.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Class to handle all the authentication logic.
 */

@Service
public class AuthenticationService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${chatclient.log.in.url}")
    private String logInUrl;

    @Value("${chatclient.log.out.url}")
    private String logOutUrl;

    // currently logged in user.
    private User currentUser;

    public boolean logIn(User user){
        String url = logInUrl + "?username=" + user.getUsername() +
                 "&password=" + user.getPassword();

        HttpEntity<Void> logInRequest = new HttpEntity<>(null);

        ResponseEntity<Boolean> loggedInResult =
                restTemplate.postForEntity(url, logInRequest, Boolean.class);

        return loggedInResult.getBody();
    }

    public void logOut(){
        String url = logOutUrl + "?username=" + this.currentUser.getUsername();
        HttpEntity<Void> logOutRequest = new HttpEntity<>(null);

        restTemplate.postForEntity(url, logOutRequest, Void.class);

        this.currentUser = null;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
