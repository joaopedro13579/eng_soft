package com.example.demo.services;

import java.sql.*;

import org.springframework.stereotype.Service;
import com.example.demo.entities.User;

@Service
public class UserService {
    // DB connection constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";
    public User getUser(String name) {
    String sql = "SELECT id, username, password FROM public.\"user\" WHERE username = ?";
    User user = new User();

    try (
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));  // ✅ use value from DB, not input
            user.setPassword(rs.getString("password"));

            System.out.println("User found: " + user.getUsername());
            System.out.println("ID: " + user.getId());

            rs.close();
            return user;
        } else {
            System.out.println("No user found with username: " + name);
            rs.close();
            return null;
        }

    } catch (SQLException e) {
        System.err.println("Database error: " + e.getMessage());
    }

    return null;
    }

    public User getUser(int id) {
        int userId = id; // Change to the ID you want to query

        String sql = "SELECT id, username, password FROM public.\"user\" WHERE id = ?";
        User user = new User();
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("User found:");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("username"));
                System.out.println("Password: " + rs.getString("password"));
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            } else {
                System.out.println("No user found with ID " + userId);
            }

            rs.close();
            return user;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return null;
    }

    public String setUser(User user) {
        String insertSQL = "INSERT INTO public.\"user\" (username, password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // Set values
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());


            // Execute the insert
            int rowsInserted = pstmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteUser(int id) {
        String deleteSQL = "DELETE FROM public.\"user\" WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            // Set the ID of the user to delete
            pstmt.setInt(1, id);

            // Execute the delete
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String name) {
        String deleteSQL = "DELETE FROM public.\"user\" WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            // Set the username of the user to delete
            pstmt.setString(1, name);

            // Execute the delete
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user,int id) {
        String updateSQL = "UPDATE public.\"user\" SET username=?, password = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            // Set the new password and user ID
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, id);

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();}
        return false;
    }

    public boolean updateUser(int id , String password) {
        String updateSQL = "UPDATE public.\"user\" SET password = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            // Set the new password and user ID
            pstmt.setString(1, password);
            pstmt.setInt(2, id);

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();}
        return false;
    }

}
