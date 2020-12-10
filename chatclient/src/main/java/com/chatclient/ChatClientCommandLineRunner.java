package com.chatclient;

import com.chatclient.service.ChatStompSessionHandler;
import com.chatclient.ui.ChatClientUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Class with code to be executed once the spring context is done
 * loading. This is where we will start our program logic, akin to the
 * the "main" method.
 */

@Component
public class ChatClientCommandLineRunner implements CommandLineRunner {
    @Autowired
    private ChatClientUI ui;

    @Autowired
    private ChatStompSessionHandler chatStompSessionHandler;

    @Value("${chatclient.websocket.server.base.url}")
    private String websocketServerUrl;

    @Override
    public void run(String... args) throws Exception {
        connectToWebSoketsServer();
        ui.start();
    }

    private void connectToWebSoketsServer(){
        WebSocketClient wsClient = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(wsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connect(websocketServerUrl, chatStompSessionHandler);
    }
}
