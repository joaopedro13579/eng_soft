package com.example.demo.services;

import com.example.demo.entities.*;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelationService {
    // DB connection constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";

    // ========== USER <-> PROJECT ==========

    public List<UserProject> getProjectByUser(long userId) {
        String sql = "SELECT * FROM public.user_project WHERE user_id = ?";
        List<UserProject> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserProject up = new UserProject();
                up.setUser(rs.getLong("user_id"));
                up.setProject(rs.getLong("project_id"));
                list.add(up);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<UserProject> getUserByProject(long projectId) {
        String sql = "SELECT * FROM public.user_project WHERE project_id = ?";
        List<UserProject> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserProject up = new UserProject();
                up.setUser(rs.getLong("user_id"));
                up.setProject(rs.getLong("project_id"));
                list.add(up);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean setProjectUser(UserProject relation) {
        String sql = "INSERT INTO public.user_project (user_id, project_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, relation.getUser());
            stmt.setLong(2, relation.getProject());

            int rows = stmt.executeUpdate();
            System.out.println("Inserted relation: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ========== USER <-> MESSAGE ==========
    public boolean deleteUserMessage(long userId, long messageId) {
        if (userId != 0 && messageId != 0) {
            String sql = "DELETE FROM public.usermessage WHERE user_id = ? AND message_id = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, userId);
                stmt.setLong(2, messageId);

                int rows = stmt.executeUpdate();
                System.out.println("Deleted relation: " + rows);
                return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (userId != 0) {
            String sql = "DELETE FROM public.usermessage WHERE user_id = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, userId);

                int rows = stmt.executeUpdate();
                System.out.println("Deleted relation: " + rows);
                return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (messageId != 0) {
            String sql = "DELETE FROM public.usermessage WHERE message_id = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, messageId);

                int rows = stmt.executeUpdate();
                System.out.println("Deleted relation: " + rows);
                return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<MessageUser> getMessageByUser(long userId) {
        String sql = "SELECT * FROM public.usermessage WHERE user_id = ?";
        List<MessageUser> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MessageUser mu = new MessageUser();
                mu.setUserId(rs.getLong("user_id"));
                mu.setMessageId(rs.getLong("message_id"));
                list.add(mu);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public MessageUser getbyboth(Long message, Long user) {
        String sql = "SELECT FROM public.usermessage WHERE user_id = ? AND message_id = ?";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql);
            MessageUser mu=new MessageUser();
            stmt.setLong(1, user);
            stmt.setLong(2, message);
            ResultSet rs = stmt.executeQuery();
            mu.setUserId(rs.getLong("user_id"));
            mu.setMessageId(rs.getLong("message_id"));
            return mu;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MessageUser> getUserByMessage(long messageId) {
        String sql = "SELECT * FROM public.usermessage WHERE message_id = ?";
        List<MessageUser> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, messageId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MessageUser mu = new MessageUser();
                mu.setUserId(rs.getLong("user_id"));
                mu.setMessageId(rs.getLong("message_id"));
                list.add(mu);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean setMessageUser(MessageUser relation) {
        String sql = "INSERT INTO public.usermessage (user_id, message_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, relation.getUserId());
            stmt.setLong(2, relation.getMessageId());

            int rows = stmt.executeUpdate();
            System.out.println("Inserted relation: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ========== PROJECT <-> MESSAGE ==========

    public List<MessageProject> getMessageByProject(long projectId) {
        String sql = "SELECT * FROM public.message_project WHERE project_id = ?";
        List<MessageProject> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MessageProject mp = new MessageProject();
                mp.setMessage(rs.getLong("message_id"));
                mp.setProject(rs.getLong("project_id"));
                list.add(mp);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<MessageProject> getProjectByMessage(long messageId) {
        String sql = "SELECT * FROM public.message_project WHERE message_id = ?";
        List<MessageProject> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, messageId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MessageProject mp = new MessageProject();
                mp.setMessage(rs.getLong("message_id"));
                mp.setProject(rs.getLong("project_id"));
                list.add(mp);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean setMessageProject(MessageProject relation) {
        String sql = "INSERT INTO public.message_project (message_id, project_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, relation.getMessage());
            stmt.setLong(2, relation.getProject());

            int rows = stmt.executeUpdate();
            System.out.println("Inserted relation: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
