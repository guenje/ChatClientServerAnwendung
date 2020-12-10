package com.chatclient.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String from;
    private String to;
    private String text;
    private MessageType messageType = MessageType.MESSAGE;

    public String toString(){
        String currentTime = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")
                .format(LocalDateTime.now());
        return from + "::> " + text + " [ " + currentTime + " ]";
    }
}
