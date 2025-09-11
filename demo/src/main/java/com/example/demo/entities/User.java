package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean setId(int id) {
        this.id = (long) id;
        return true;
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    };

    public Boolean setPassword(String password) {
        if (password.length() <= 30) {
            this.password = password;
            return true;
        } else {
            return false;
        }
    };
    public User(){};
}
