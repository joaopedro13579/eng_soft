package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.authenticators.Authenticator;
import com.example.demo.services.MessageService;
import com.example.demo.services.RelationService;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private RelationService relationservice;

    @GetMapping("/{id}")
    public Message getMessage(@PathVariable int id, @RequestHeader("Authorization") String token) {
        try {
            Message message = messageService.getMessage(id);
            return message;
        } catch (Exception e) {
            throw new RuntimeException("Database error", e);

        }

    }

    @PostMapping("/")
    public Message createMessage(@RequestBody Message message, @RequestHeader("Authorization") String token) {
            try {
                return messageService.createMessage(message);
            } catch (Exception e) {
                throw new RuntimeException("Database error", e);
            }
    }

    @PutMapping("/{id}")
    public boolean updateMessage(@PathVariable int id, @RequestBody Message updated,
            @RequestHeader("Authorization") String token) {
        Message currentMessage = messageService.getMessage(id);
        if (authenticator.autorize(token, currentMessage)) {
            try {
                return messageService.updateMessage(id, updated);
            } catch (Exception e) {
                throw new RuntimeException("Database error", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
    @PutMapping("/participants/{id}")
    public boolean changeParticipant(@PathVariable int id, @RequestBody List<MessageUser> participants,
            @RequestHeader("Authorization") String token) {
        Message currentMessage = messageService.getMessage(id);
        if (authenticator.autorize(token, currentMessage)) {
            try {
                return messageService.updateparticipants(participants);
            } catch (Exception e) {
                throw new RuntimeException("Database error", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

    }
    @DeleteMapping("/{id}")
    public boolean deleteMessage(@PathVariable int id, @RequestHeader("Authorization") String token) {
        Message message = messageService.getMessage(id);
        System.out.println(message.getText());
        try {authenticator.autorize(token, message);
            return messageService.deleteMessage(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }    @GetMapping("/user/{userId}")
    public List<Message> getMessagesByUser(@PathVariable int userId, @RequestHeader("Authorization") String token) {
        try {
            List<MessageUser> relations = relationservice.getMessageByUser(userId);
            List<Message> messages = new ArrayList<>();
            for (MessageUser mu : relations) {
                // assume MessageUser has getMessageId(); if it has getMessage() adjust accordingly
                messages.add(messageService.getMessage((mu.getMessageId()).intValue()));
            }
            return messages;
        } catch (Exception e) {
            throw new RuntimeException("Database error", e);
        }
    }
}
