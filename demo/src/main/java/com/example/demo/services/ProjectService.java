package com.example.demo.services;

import java.sql.*;

import org.ietf.jgss.MessageProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.User;
import com.example.demo.entities.Project;
import com.example.demo.entities.UserProject;
import com.example.demo.services.RelationService;
import com.example.demo.entities.Message;
import com.example.demo.entities.MessageProject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    // DB connection constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";
    @Autowired
    public RelationService relationService =new RelationService();

    public Project getProject(int id) {
        int projectId = id; // Change to the ID you want to query

        String sql = "SELECT id, description FROM public.project WHERE id = ?";
        Project project = new Project();
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projectId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                project.setId(rs.getLong("id"));
                project.setDescription(rs.getString("description"));
                System.out.println("Project found: " + project.getDescription());
                System.out.println("ID: " + project.getId());
                rs.close();
            } else {
                System.out.println("No project found with id: " + id);
                rs.close();
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        try {
            List<UserProject> userproject = relationService.getUserByProject(id);
            List<User> user = new java.util.ArrayList<>();
            for (UserProject up : userproject) {
                System.out.println("UserProject ID: " + up.getUser() + ", " + up.getProject());
                User u = new User();
                u.setId(up.getUser());
                System.out.println("User ID: " + u.getId());
                user.add(u);
            }
            project.setParticipant(user);
            System.out.println("user->projects gotten");
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }
        try {
            List<MessageProject> messageproject = relationService.getMessageByProject(id);
            List<Message> messages = new java.util.ArrayList<>();
            for (MessageProject mp : messageproject) {
                Message m = new Message();
                m.setId(mp.getMessage());
                System.out.println("Message ID: " + m.getId());
                messages.add(m);
            }
            project.setMessages(messages);
            System.out.println("messages->projects gotten");
            return project;
        } catch (Exception e) {
            throw new RuntimeException("Database error", e);
        }

    }

    public Project createProject(Project description) {
        String sql = "INSERT INTO public.project (description) VALUES (?) RETURNING id, description";
        Project project = new Project();

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, description.getDescription());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                project.setId(rs.getLong("id"));
                project.setDescription(rs.getString("description"));

                System.out.println("Project created: " + project.getDescription());
                System.out.println("ID: " + project.getId());

                rs.close();

            } else {
                System.out.println("Project creation failed for description: " + description);
                rs.close();
                return null;
            }
            try {
                for (Message m : description.getMessages()) {
                    MessageProject mp = new MessageProject();
                    mp.setProject(project.getId());
                    mp.setMessage(m.getId());
                    System.out.println("Associating message ID " + m.getId() + " with project ID " + project.getId());
                    relationService.setMessageProject(mp);
                }
            } catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
            }
            try {
                for (User u : description.getParticipant()) {
                    UserProject up = new UserProject();
                    up.setProject(project.getId());
                    up.setUser(u.getId());
                    System.out.println("Associating user ID " + u.getId() + " with project ID " + project.getId());
                    relationService.setProjectUser(up);
                }}catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
                }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return null;
    }

    public boolean deleteProject(int id) {
        String sql = "DELETE FROM public.project WHERE id = ?";
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Project with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No project found with ID " + id + ". Deletion failed.");
            }
            //delete relations
            try {
                List<UserProject> userproject = relationService.getUserByProject(id);
            for (UserProject up : userproject) {
                relationService.deleteUserProject(up.getUser(), id);
            }
            List<MessageProject> messageproject = relationService.getMessageByProject(id);
            for (MessageProject mp : messageproject) {
                relationService.deleteMessageProject(id, mp.getMessage());
            }
            } catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
public boolean updateProject(int id, Project projectData) {
    String updateSql = "UPDATE public.project SET description = ? WHERE id = ?";
    System.out.println("Updating project with ID: " + id);

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(updateSql)) {

        // --- 1️⃣ Update the project's basic info
        stmt.setString(1, projectData.getDescription());
        stmt.setInt(2, id);
        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected == 0) {
            System.out.println("No project found with ID " + id + ". Update failed.");
            return false;
        }

        // --- 2️⃣ Handle related messages (replace with new set)
        List<MessageProject> existingMessages = relationService.getMessageByProject(id);
        for (MessageProject mp : existingMessages) {
            relationService.deleteMessageProject(id, mp.getMessage());
        }
        for (Message msg : projectData.getMessages()) {
            MessageProject mp = new MessageProject();
            mp.setProject(id);
            mp.setMessage(msg.getId());
            relationService.setMessageProject(mp);
        }


        // --- 3️⃣ Delete old User
        for (UserProject up : relationService.getUserByProject(id)) {
            relationService.deleteUserProject(up.getUser(), id);
        }

        // --- 4️⃣ Add new Users
        for (User u : projectData.getParticipant()) {
            UserProject up = new UserProject();
            up.setProject(id);
            up.setUser(u.getId());
            relationService.setProjectUser(up);
        }
        
        System.out.println("✅ Project updated successfully with all relations.");
        return true;

    } catch (SQLException e) {
        System.err.println("❌ SQL Error while updating project: " + e.getMessage());
        return false;
    }
}

}