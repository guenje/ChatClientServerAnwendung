package com.chatclient.service;

import com.chatclient.ui.ChatClientUI;
import com.chatclient.ui.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Handle all issues related to chatting.
 */

@Service
public class ChatService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ChatStompSessionHandler chatStompSessionHandler;

    @Autowired
    private ChatClientUI chatClientUI;

    private final Random random = new Random();

    // the chat patner in the current
    // conversation (if any).
    private User currentChatPatner;

    // the last time the current chat patner was seen.
    private LocalDateTime currentChatPatnerLastSeen;

    private boolean isCaller = false;

    @Value("${chatclient.users.logged.in.url}")
    private String loggedInUsersUrl;

    public List<User> getLoggedInUsers() {
        User[] loggedInUsersArray = restTemplate.getForObject(loggedInUsersUrl, User[].class);
        return Arrays.asList(loggedInUsersArray);
    }

    public User getCurrentChatPatner() {
        return currentChatPatner;
    }

    public boolean isCaller() {
        return isCaller;
    }

    public void setCaller(boolean caller) {
        isCaller = caller;
    }

    /**
     * Invalidate any information partaining to a
     * chat session.
     */
    public void invalidateCurrentSession(){
        this.setCaller(false);
        this.currentChatPatnerLastSeen = null;
        this.currentChatPatner = null;
    }

    /**
     * Process the chat message received from the server.
     *
     * @param message The chat message to be processed.
     */
    public void processChatMessage(Message message) {
        if(message.getMessageType() == MessageType.PRESENCE && this.currentChatPatner != null){
            if(message.getFrom().equals(currentChatPatner.getUsername())){
                // is the PRESENCE message coming from the current chat patner? If
                // so update the last time they were seen as *now*
                this.currentChatPatnerLastSeen = LocalDateTime.now();
            }
        }

        User currentUser = authenticationService.getCurrentUser();
        if (!message.getTo().equals(currentUser.getUsername())) {
            // the message was not intended for this user, so ignore it.
            return;
        }

        if (message.getFrom().equals(currentUser.getUsername())) {
            // message is from the current user. No need to process it.
            return;
        }


        if (message.getMessageType() == MessageType.REJECT_CONNECTION && this.isCaller) {
            this.invalidateCurrentSession();
            System.out.println("\nSystem ::> " + message.getFrom() + " is currently busy on another chat.");
            return;
        }


        if (currentChatPatner == null) {
            if (message.getMessageType() == MessageType.REQUEST_CONNECTION && !this.isCaller) {
                // the person sending the message wishes to connect with the current user.
                User user = new User(message.getFrom(), "");
                this.currentChatPatner = user;

                // notify the sender that the request was OK, and the chat can begin.
                Message m = new Message();
                m.setMessageType(MessageType.ACCEPT_CONNECTION);
                m.setTo(message.getFrom());
                m.setFrom(authenticationService.getCurrentUser().getUsername());

                chatStompSessionHandler.sendMessage(m);

                System.out.print("\nSystem ::> Hit enter to begin chat with: " + m.getTo());
                return;
            }

            if (message.getMessageType() == MessageType.ACCEPT_CONNECTION && isCaller) {
                // the person sending the 'accept connection' has accepted a chat with the current user.
                User user = new User(message.getFrom(), "");
                this.currentChatPatner = user;
                System.out.print("\nSystem ::> Hit enter to begin chat with: " + message.getFrom());
                return;
            }
        }


        if (currentChatPatner != null) {
            if (message.getMessageType() == MessageType.DISCONNECT &&
                    message.getFrom().equals(currentChatPatner.getUsername())) {
                // the current chat partner wishes to end chat. Nullify the
                // existing chat session..
                invalidateCurrentSession();
                System.out.print("\nSystem ::> " + message.getFrom() + " has ended chat. Hit enter to continue.");
                return;
            }

            if (message.getMessageType() == MessageType.MESSAGE &&
                    message.getFrom().equals(currentChatPatner.getUsername())) {
                // the current chat partner has sent a message.
                System.out.println();
                System.out.println(message);
                System.out.print(authenticationService.getCurrentUser().getUsername() + " ::>");
                return;
            }

            if (message.getMessageType() == MessageType.REQUEST_CONNECTION) {
                // the sender wants a connection, but the current user is busy on a chat.
                Message m = new Message();
                m.setTo(message.getFrom());
                m.setFrom(authenticationService.getCurrentUser().getUsername());
                m.setMessageType(MessageType.REJECT_CONNECTION);

                sendMessage(m);
            }
        }
    }

    /**
     * Periodically check whether the current chat partner (if present) has a connection hitch.
     */
    @Scheduled(fixedRate = 3000)
    public void checkChatPartnerOnlinePresence(){
        if(currentChatPatner == null){
            return;
        }

        if(currentChatPatnerLastSeen == null){
            return;
        }

        long lastSeenInSeconds = Duration.between(currentChatPatnerLastSeen, LocalDateTime.now()).getSeconds();
        if(lastSeenInSeconds > 6){
            System.out.println("\nSystem ::> " + currentChatPatner.getUsername() + " was last seen " + lastSeenInSeconds + " seconds ago.");
            System.out.print(authenticationService.getCurrentUser().getUsername() + " ::>");
        }

        if(lastSeenInSeconds > 60){
            System.out.println("\nSystem ::> " + currentChatPatner.getUsername() + " was last seen " + lastSeenInSeconds + " seconds ago." +
                    "Ending chat automatically.");
            this.invalidateCurrentSession();
        }
    }



    /**
     * Periodically send online presence.
     * The function sleeps for a number of seconds before
     * sending the online presence in order to simulate a
     * user going on and off.
     */
    @Scheduled(fixedDelay = 2000)
    public void sendOnlinePresences(){
        try{
            int seconds = random.nextInt(10);
            if(authenticationService.getCurrentUser() != null){
                Message message = new Message();
                message.setTo("all");
                message.setText("");
                message.setFrom(authenticationService.getCurrentUser().getUsername());
                message.setMessageType(MessageType.PRESENCE);

                sendMessage(message);
            }
            TimeUnit.SECONDS.sleep(seconds);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(Message message) {
        this.chatStompSessionHandler.sendMessage(message);
    }

}
