package com.chatserver.controller;

import java.util.Objects;

public class Message {
    private String from;
    private String to;
    private String text;
    private MessageType messageType = MessageType.MESSAGE;

    public Message(String from, String to, String text, MessageType messageType) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.messageType = messageType;
    }

    public Message() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(getFrom(), message.getFrom()) &&
                Objects.equals(getTo(), message.getTo()) &&
                Objects.equals(getText(), message.getText()) &&
                getMessageType() == message.getMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo(), getText(), getMessageType());
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", text='" + text + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
