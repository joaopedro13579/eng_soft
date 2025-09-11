package com.example.demo.services;

import java.sql.*;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Message;

@Service
public class MessageService {
    // DB connection constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";

    public Message getMessage(int id) {
        String sql = "SELECT id, content FROM public.message WHERE id = ?";
        Message message = new Message();

        try (
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                message.setId(rs.getLong("id"));
                message.setText(rs.getString("content"));

                System.out.println("Message found: " + message.getText());
                rs.close();
                return message;
            } else {
                System.out.println("No message found with id: " + id);
                rs.close();
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return null;
    }

    public Message createMessage(Message content) {
        String sql = "INSERT INTO public.message (content) VALUES (?) RETURNING id, content";
        Message message = new Message();

        try (
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, content.getText());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                message.setId(rs.getLong("id"));
                message.setText(rs.getString("content"));

                System.out.println("Message created: " + message.getText());
                rs.close();
                return message;
            } else {
                System.out.println("Message creation failed for content: " + content);
                rs.close();
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return null;
    }

    public boolean deleteMessage(int id) {
        String sql = "DELETE FROM public.message WHERE id = ?";

        try (
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Message with ID " + id + " deleted successfully.");
                return true;
            } else {
                System.out.println("No message found with ID " + id + ". Deletion failed.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMessage(int id, Message content) {
        String sql = "UPDATE public.message SET content = ? WHERE id = ?";

        try (
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, content.getText());
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Message with ID " + id + " updated successfully.");
                return true;
            } else {
                System.out.println("No message found with ID " + id + ". Update failed.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
}
