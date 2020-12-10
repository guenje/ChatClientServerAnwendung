package com.chatserver.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Class to model a user.
 */

public class User {
    private String username;
    @JsonIgnore
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}
