package com.example.demo.services;
import com.example.demo.services.*;
import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.*;

@Service
public class RelationService {
    // DB connection constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";
    public List<UserProject> getProjectbyUser(Long id){//to do
        return null;
    }
    public List<UserProject> getProjectbyUser(int id) {//to do
        return null;
    }
    public List<UserProject> getUserbyProject(int id) {//to do
        return null;
    }
    public List<UserProject> getUserbyProject(Long id){//to do
        return null;
    }
    public List<MessageUser> getMessagebyUser(int id) {
        //To do
        return null;
    }
    public List<MessageUser> getMessagebyUser(Long id){
        //To do
        return null;
    }
    public List<MessageUser> getUserbyMessage(int id){
        //To Do 
        return null;
    }
    public List<MessageUser> getUserbyMessage(Long id){
        //To Do 
        return null;
    }
    public List<MessageProject> getMessagebyProject(Long id){
        //To Do 
        return null;
    }
    public List<MessageProject> getMessagebyProject(int id) {
        //To Do 
        return null;
    }
    public List<MessageProject> getProjectbyMessage(Long id){
        //To Do 
        return null;
    }
    public List<MessageProject> getProjectbyMessage(int id) {
        //To Do 
        return null;
    }
//setters
    public void setProjectUser(UserProject content){//to do
        
    }

    public void setMessageUser(MessageUser content) {
        //To do
        
    }
    public void setMessageProject(MessageProject user){
        //To Do 
        
    }
}
