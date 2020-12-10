package com.chatclient.ui;

import com.chatclient.service.AuthenticationService;
import com.chatclient.service.ChatService;
import com.chatclient.service.Message;
import com.chatclient.service.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Handle all the UI logic.
 */

@Component
public class ChatClientUI {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ChatService chatService;

    public void start() {
        showMainHeader();
        logIn();
    }

    public void showMainMenu() {
        println("\n");
        showMainHeader();

        String loggedInUsername = authenticationService
                .getCurrentUser()
                .getUsername();

        println("---------------- Welcome: " + loggedInUsername + "----------------");

        String userMenuChoice = "";
        Scanner scanner = new Scanner(System.in);
        while (true){
            println("------- Main Menu -------");
            println("1. Show logged in users.");
            println("2. Start Chat.");
            println("3. Log Out.");

            print("Enter your choice ::> ");
            userMenuChoice = scanner.nextLine().trim();

            if(chatService.getCurrentChatPatner() != null){
                userMenuChoice = "4";
            }

            switch (userMenuChoice) {
                case "1":
                    showLoggedInUsers();
                    break;
                case "2":
                    initiateChat();
                    break;
                case "3": {
                    logOut();
                    break;
                }
                case "4":
                    launchChatSession();
                    break;
                default:
                    println("\nInvalid choice. Check and try again.\n");
            }

            if(userMenuChoice.equals("3")){
                // the user's choice was to log out. Exit the main menu loop.
                break;
            }
        }
    }


    // ### Utilty methods ###
    private void print(Object object) {
        System.out.print(object);
    }

    private void println(Object object) {
        System.out.println(object);
    }

    private void logIn() {
        String enteredUsername = "";
        Scanner scanner = new Scanner(System.in);

        while (true) {
            println("-------------- Log In ----------------");
            print("Enter your username ::>");
            String username = scanner.nextLine().trim();

            print("Enter your password ::>");
            String password = scanner.nextLine().trim();

            if (username.equals("exit")) {
                break;
            }

            try {
                User user = new User(username, password);
                boolean logInSuccess = authenticationService.logIn(user);
                if (logInSuccess) {
                    authenticationService.setCurrentUser(user);
                    break;
                } else {
                    println("\nError: Invalid Credentials. Try Again.\n");
                }
            } catch (Exception e) {
                println(e.getMessage());
            }
        }

        if (enteredUsername.equals("exit")) {
            // the user exited the loop inorder to quit the program
            println("Bye.");
            return;
        }

        // the user has logged in successfully. So they are to be directed to the main interface.
        showMainMenu();
    }

    private void logOut(){
        try {
            authenticationService.logOut();
        } catch (Exception e) {
            println(e.getMessage());
        }
        println("Bye.");
        System.exit(0);
    }

    private void showMainHeader() {
        println("\n=============== WELCOME TO THE CHAT WORLD =================");
    }

    private void showLoggedInUsers(){
        try {
            List<User> users = chatService.getLoggedInUsers();
            println("\n***** Logged in users *****");
            for(int i = 0; i < users.size(); i++){
                println(i + 1 + "." + users.get(i).getUsername());
            }
            println("");
        } catch (Exception e){
            println(e.getMessage());
        }
    }

    private void initiateChat(){
        Scanner s = new Scanner(System.in);
        println("\n\n-------------- Chat Mode ----------------");
        print("Enter the name of your chat partner ::>");
        String partnerName = s.nextLine();
        String currentUserName = authenticationService.getCurrentUser().getUsername();

        if(partnerName.equals(currentUserName)){
            println("\nError: You cannot chat with yourself. Check and try again.\n");
            return;
        }

        // ensure the selected partner has already logged in.
        List<User> loggedInUsers = this.chatService.getLoggedInUsers();
        Optional<User> loggedInPartnerOptional = loggedInUsers.stream()
                .filter(user -> user.getUsername()
                        .equals(partnerName))
                .findFirst();

        if(!loggedInPartnerOptional.isPresent()){
            println("\nError: " + partnerName + " has not yet logged in.\n");
            return;
        }

        // create a REQUEST_CONNECTION message to the target partner.
        Message message = new Message();
        message.setFrom(currentUserName);
        message.setTo(loggedInPartnerOptional.get().getUsername());
        message.setMessageType(MessageType.REQUEST_CONNECTION);
        message.setText("");

        // send the message to the partner to request chat
        chatService.sendMessage(message);

        println("System ::> Inviting " + loggedInPartnerOptional.get().getUsername() + " for a chat. Please wait.");
        println("");
        chatService.setCaller(true);
    }

    public void launchChatSession(){
        Scanner scanner2 = new Scanner(System.in);
        String currentUsername = authenticationService.getCurrentUser().getUsername();
        String currentChatPartner = chatService.getCurrentChatPatner().getUsername();

        println("\n--------- Chat Session. Enter 'exit' to quit chat -------------");
        while(chatService.getCurrentChatPatner() != null){
            print(currentUsername + " ::> ");
            String message = scanner2.nextLine().trim();

            if(message.equalsIgnoreCase("exit")){
                // the user wishes to end the session
                this.chatService.invalidateCurrentSession();

                Message m = new Message();
                m.setTo(currentChatPartner);
                m.setFrom(currentUsername);
                m.setText("exit");
                m.setMessageType(MessageType.DISCONNECT);
                chatService.sendMessage(m);

                println("Session has been terminated.");
                break;
            }


            Message m = new Message();
            m.setTo(currentChatPartner);
            m.setFrom(currentUsername);
            m.setText(message);
            m.setMessageType(MessageType.MESSAGE);

            chatService.sendMessage(m);
        }
    }
}
