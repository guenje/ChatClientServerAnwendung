package com.chatclient.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to model a user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String password;
}
