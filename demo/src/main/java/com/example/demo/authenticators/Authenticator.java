package com.example.demo.authenticators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.demo.services.ProjectService;
import com.example.demo.services.UserService;
import com.example.demo.controllers.ProjectController;
import com.example.demo.entities.*;
import com.example.demo.services.ProjectService;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Authenticator {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";
    @Autowired
    UserService userService;

    public boolean autorize(Key asker, Key target) {
        return true;
    }

    public boolean autorize(int asker, int target) {
        return true;
    }

    public boolean autorize(Key asker, int target) {
        return true;
    }

    public boolean autorize(int asker, Key target) {
        return true;
    }

    public boolean autorize(String token, Project project) {
        return true;
    }

    public boolean autorize(String token, Message message) {
        return true;

    }

    private String keygen(Key key) {
        String token;
        Date date = new Date();
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        int first = random.nextInt(randomNumber);
        int second = random.nextInt(randomNumber - first);
        int third = random.nextInt(randomNumber - (first + second));
        int fourth = random.nextInt(randomNumber - (first + second + third));
        int fifth = randomNumber - (first + second + third + fourth);
        long time = date.getTime();
        token = Long.toString(time) + ":" + key.getId() +
                ":" + first + "." + second + "." + third + "." + fourth + "." + fifth + "." + randomNumber;
        return token;
    }

    public String createKey(Key key) {
        String sql = "INSERT INTO public.auth (authkeys,userid,level) VALUES (?,?,?)";
        String token = keygen(key);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setLong(2, key.getId());
            stmt.setInt(3, key.getLevel());
            ResultSet rs = stmt.executeQuery();
            rs.close();
            return token;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return token;
    }

    public Key get(Long userId) {
        String sql = "SELECT authkeys, level FROM auth WHERE userid = ?";
        Key key = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                key = new Key();
                key.setId(userId);
                key.setKey(rs.getString("authkeys"));
                key.setLevel(rs.getInt("level"));
            }

            rs.close();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return key;
    }

    public Key get(String token) {
        String sql = "SELECT userid , level FROM auth WHERE authkeys = ?";
        Key key = null;
        System.out.println("Retrieving key for token from the database: " + token);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                key = new Key();
                key.setId(rs.getLong("userid"));
                key.setKey(token);
                key.setLevel(rs.getInt("level"));
            }

            rs.close();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return key;
    }

    public boolean update(Long userId, int newLevel) {
        String sql = "UPDATE auth SET level = ? WHERE userid = ?";
        boolean updated = false;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newLevel);
            stmt.setLong(2, userId);

            int rows = stmt.executeUpdate();
            updated = (rows > 0);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return updated;
    }

}
