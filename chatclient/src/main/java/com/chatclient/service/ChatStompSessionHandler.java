package com.chatclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class ChatStompSessionHandler extends StompSessionHandlerAdapter {
    private StompSession stompSession;

    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // save the stomp session to allow sending message later on.
        this.stompSession = session;

        // subscribe to the server websockets URL.
        this.stompSession.subscribe("/topic/messages", this);
    }

    // send a message to the chat end point.
    public void sendMessage(Message message){
        this.stompSession.send("/app/chat", message);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        chatService.processChatMessage((Message) payload);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }
}
