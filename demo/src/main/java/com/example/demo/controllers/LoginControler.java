package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.authenticators.Authenticator;
import com.example.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/login")
public class LoginControler {
    @Autowired
    private Authenticator autenticator;
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public Key login(@RequestBody User user) {
        Key key = new Key();
        key.setId(userService.getUser(user.getUsername()).getId());
        String rc = user.getPassword().trim();
        String db = userService.getUser(user.getUsername()).getPassword().trim();
        System.out.println(db + ":" + rc);
        if (rc.equals(db)) {
            String token = autenticator.createKey(key);
            key.setKey(token);
            System.out.println(key.getKey());
            return key;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    @PostMapping("/manager/{id}")
    public boolean managerLogin(@RequestParam String key, @PathVariable Long id) {
        Key requesterKey = autenticator.get(key); // Get key from token string
        Key targetKey = autenticator.get(id); // Get key from user ID

        if (requesterKey == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid requester token");
        }
        if (targetKey == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No key found for user id: " + id);
        }

        int requesterLevel = requesterKey.getLevel();
        int targetLevel = targetKey.getLevel();

        System.out.println("Requester level: " + requesterLevel);
        System.out.println("Target user level: " + targetLevel);

        if (requesterLevel > targetLevel) {
            try {
                autenticator.update(id, targetLevel + 1); // Increase target's level
                return true;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user level");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Upgrade not allowed: insufficient level");
        }
    }
}
