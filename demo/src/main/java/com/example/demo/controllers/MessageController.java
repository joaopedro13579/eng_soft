package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.authenticators.Authenticator;
import com.example.demo.services.MessageService;

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
    public Message createMessage(@RequestBody Message message, @RequestHeader("Auth") String token) {
        if (true) {
            try {
                return messageService.createMessage(message);
            } catch (Exception e) {
                throw new RuntimeException("Database error", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PutMapping("/{id}")
    public boolean updateMessage(@PathVariable int id, @RequestBody Message updated,
            @RequestHeader("Auth") String token) {
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
    public boolean deleteMessage(@PathVariable int id, @RequestHeader("Auth") String token) {
        Message message = messageService.getMessage(id);
        if (authenticator.autorize(token, message)) {
            return messageService.deleteMessage(id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

}
