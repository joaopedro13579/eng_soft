package com.example.demo.entities;

import java.util.List;

public class Project {
    private String description;
    private Long id;
    private  List<User> participant;
    private List<Message> messages;
    // Getter and Setter
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getParticipant(int id) {
        return participant.get(id)  ;   
    }
    public List<User> getParticipant() {
        return participant;
    }
    public void setParticipant(User participant) {
        this.participant.add(participant);
    }
    public Message getMessages(int id) {
        return messages.get(id)  ;   
    }
    public List<Message> getMessages() {
        return messages;    
    }
    public void setMessages(Message message) {
        this.messages.add(message);
    }
}
