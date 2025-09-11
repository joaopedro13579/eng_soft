package com.example.demo.services;

import java.sql.*;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Project;

@Service
public class ProjectService {
    // DB connection constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "main";
    private static final String DB_PASSWORD = "18410209";

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
                return project;
            } else {
                System.out.println("No project found with id: " + id);
                rs.close();
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return null;
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
                return project;
            } else {
                System.out.println("Project creation failed for description: " + description);
                rs.close();
                return null;
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
                return true;
            } else {
                System.out.println("No project found with ID " + id + ". Deletion failed.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProject(int id, Project description) {
        String sql = "UPDATE public.project SET description = ? WHERE id = ?";
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, description.getDescription());
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Project with ID " + id + " updated successfully.");
                return true;
            } else {
                System.out.println("No project found with ID " + id + ". Update failed.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
}