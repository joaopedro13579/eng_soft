package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.authenticators.*;
import com.example.demo.services.UserService;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.services.ProjectService;

public class ProjectControler {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private Authenticator autenticator;

    @GetMapping("/{id}")
    public Project getproject(@PathVariable int id, @RequestHeader("Auth") String token) {
        Project project = new Project();
        try {
            project = projectService.getProject(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged");

        }

        if (autenticator.autorize(token, project)) {
            return project;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PostMapping("/")
    public Project createProject(@RequestBody Project description, @RequestHeader("Auth") String token) {
        if (true) {
            try {
                return projectService.createProject(description);
            } catch (Exception e) {
                throw new RuntimeException("database error:",e);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PutMapping("/{id}")
    public boolean updateproject(@PathVariable int id, @RequestBody Project description,
            @RequestHeader("Auth") String token) {
        Project updatedProject = projectService.getProject(id);
        if (autenticator.autorize(token, updatedProject)) {
            try {
                return projectService.updateProject(id, description);
            } catch (Exception e) {
                throw new RuntimeException("data base error", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        }
    }

    @DeleteMapping("/{id}")
    public boolean deleteProject(@RequestParam int id, @RequestHeader("Auth") String token) {
        if (autenticator.autorize(token, projectService.getProject(id))) {
            return projectService.deleteProject(id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

    }
}
