package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.authenticators.Authenticator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.services.RelationService;
import com.example.demo.services.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private Authenticator autenticator;

    @GetMapping("/{id}")
    public Project getProject(@PathVariable int id, @RequestHeader("Authorization") String token) {
        Project project;
        try {
            project = projectService.getProject(id);
            List<UserProject> up =relationService.getUserByProject(id);
            List<User> users = new java.util.ArrayList<>();
            for(UserProject u : up){
                User user = new User();
                user.setId(u.getUser());
                users.add(user);
            }
            project.setParticipant(users);
            List<MessageProject> m=relationService.getMessageByProject(id);
            List<Message> messages = new java.util.ArrayList<>();
            for(MessageProject msg : m){
                Message message = new Message();
                message.setId(msg.getMessage());
                messages.add(message);
            }
            project.setMessages(messages);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        if (true) {
            return project;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PostMapping("/")
    public Project createProject(@RequestBody Project description, @RequestHeader("Authorization") String token) {
        try {
            System.out.println("Creating project with description: " + description.getDescription());
            return projectService.createProject(description);
        } catch (Exception e) {
            throw new RuntimeException("Database error:", e);
        }
    }

    @PutMapping("/{id}")
    public boolean updateProject(@PathVariable int id, @RequestBody Project description,
            @RequestHeader("Authorization") String token) {
        projectService.updateProject(id, description);
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean deleteProject(@PathVariable int id, @RequestHeader("Authorization") String token) {
        if (autenticator.autorize(token, projectService.getProject(id))) {
            return projectService.deleteProject(id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
}
