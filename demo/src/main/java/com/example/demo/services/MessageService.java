package com.example.demo.services;

import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.demo.entities.User;
import com.example.demo.entities.Message;
import com.example.demo.services.RelationService;
import com.example.demo.entities.MessageUser;

@Service
public class MessageService {
    // DB connection constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";
    @Autowired
    public RelationService relationService;

    public Message getMessage(int id) {
        String sql = "SELECT id, message, creator, type FROM public.message WHERE id = ?";

        Message message = new Message();

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            List<MessageUser> rel = relationService.getUserByMessage(id);
            System.out.println(rel);
            List<User> participant = new java.util.ArrayList<>();
            for (MessageUser mu : rel) {
                User user = new User();
                user.setId(mu.getUserId());
                participant.add(user);
            }
            message.setParticipant(participant);
            if (rs.next()) {
                message.setId(rs.getLong("id"));
                message.setText(rs.getString("message"));
                message.setUserId(rs.getLong("creator"));
                message.setType(rs.getString("type"));
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
        String sql = "INSERT INTO public.message (message,creator,type) VALUES (?,?,?) RETURNING id, message";
        Message message = new Message();
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, content.getText());
            stmt.setLong(2, content.getUserId());
            stmt.setString(3, content.getType());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                message.setId(rs.getLong("id"));
                message.setText(rs.getString("message"));

                System.out.println("Message created: " + message.getText());
                rs.close();
            } else {
                System.out.println("Message creation failed for content: " + content);
                rs.close();
                return null;
            }
            List<User> participant = content.getParticipant();
            MessageUser mu = new MessageUser();
            for (User user : participant) {
                System.out.println(user.getId());
                mu.setMessageId(message.getId());
                mu.setUserId(user.getId());
                try {
                    relationService.setMessageUser(mu);
                } catch (Exception e) {
                    System.out.println(e + "," + mu.getUserId() + "," + mu.getMessageId());
                }
            }
            return message;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return null;
    }

    public boolean deleteMessage(int id) {
        String sql = "DELETE FROM public.message WHERE id = ?";

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Message with ID " + id + " deleted successfully.");

            } else {
                System.out.println("No message found with ID " + id + ". Deletion failed.");

            }
            List<MessageUser> rel = relationService.getUserByMessage(id);
            try {
                for (MessageUser mu : rel) {
                    relationService.deleteUserMessage((long) 0, (long) mu.getMessageId());
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMessage(int id, Message content) {
        String sql = "UPDATE public.message SET content = ? WHERE id = ?";

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public boolean updateparticipants(List<MessageUser> participants)
    {
        for (MessageUser messageUser : participants) {
            if (relationService.getMessageByUser(messageUser.getUserId())==null) {
                relationService.setMessageUser(messageUser);       
            }else{
                try{
                    relationService.deleteUserMessage(messageUser.getUserId(),messageUser.getMessageId());
                }
                catch(Exception e){
                    return false;
                }
            }    
        }
        return true;
    }
}
