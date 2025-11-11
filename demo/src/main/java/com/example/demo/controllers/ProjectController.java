package com.example.demo.controllers;

import com.example.demo.entities.*;

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

            return project;
  
    }
    @GetMapping("/user/{id}")
    public List<Project> getProjectsByUser(@PathVariable int id, @RequestHeader("Authorization") String token) {
        try {
            List<UserProject> up = relationService.getProjectByUser(id);
            List<Project> projects = new java.util.ArrayList<>();
            for(UserProject u : up){
                int projectId = Math.toIntExact(u.getProject());
                Project p = projectService.getProject(projectId);
                projects.add(p);
            }
            return projects;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projects not found for user id: " + id);
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
        
        return projectService.deleteProject(id);
    }
}
