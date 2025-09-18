package com.example.demo.entities;

import java.util.List;

public class Message {
    private String text;
    private String type;
    private Long id;
    private Long userId;
    private Long projectId;
    List<User> participant;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    // Getter and Setter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public User getParticipant(Long id) {
        for (int i = 0; i < participant.size(); i++) {            
            if (id==participant.get(i).getId()) {
                return participant.get(i);
            }
        }
        return null;
    }
    public List<User> getParticipant() {
        return participant;
    }
    public void addParticipant(User participant) {
        this.participant.add(participant);
    }
    public void setParticipant(List<User> participant) {
        this.participant = participant;
    }
}