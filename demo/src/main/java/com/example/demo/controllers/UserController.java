package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.authenticators.*;
import com.example.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Authenticator authenticator;
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id, @RequestHeader("Authorization") String token) {
        Key key;
        System.out.println("token received: " + token);
        try {
            key = authenticator.get(token);
            System.out.println("id retrieved from token: " + key.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged");
        }
        if (authenticator.autorize(key, id)) {
            return userService.getUser(id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unAuthorized due to low permission");
        }
    }

    @GetMapping("/by-name/{name}")
    public User getUserByName(@PathVariable String name, @RequestHeader("Authorization") String token) {
        Key key;
        try {
            key = authenticator.get(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not logged");

        }
        User user = userService.getUser(name);
        if (authenticator.autorize(key, user.getId().intValue())) {
            return userService.getUser(name);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PostMapping("/")
    public String createUser(@RequestBody User user) {
        return userService.setUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestParam("Authorization") String token,@PathVariable  int id, @RequestBody User user) {
        Key key;
        try {
            key = authenticator.get(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "app not logged");
        }
        if (authenticator.autorize(key, id)) {
            if (userService.updateUser(user,id)) {
                return ResponseEntity.ok("User updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PutMapping("/password/{id}")
    public boolean updatePassword(@RequestParam("Authorization") String token, @RequestBody User Password,
            @PathVariable int id) {
        Key key;
        try {
            key = authenticator.get(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "app not logged");
        }
        if (authenticator.autorize(key, id)) {
            return userService.updateUser(id, Password.getPassword());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@RequestParam("Authorization") String token, @PathVariable int id) {
        Key key;
        try {
            key = authenticator.get(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "app not logged");
        }
        if (authenticator.autorize(id, key)) {
            return userService.deleteUser(id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @DeleteMapping("/by-name/{name}")
    public boolean deleteUserByName(@RequestParam("Authorization") String token, @PathVariable String name) {
        Key key;
        try {
            key = authenticator.get(token);
            System.out.println("id retrieved from token: " + key.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "app not logged");
        }
        int id = userService.getUser(name).getId().intValue();
        if (authenticator.autorize(id, key)) {
            return userService.deleteUser(id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

}