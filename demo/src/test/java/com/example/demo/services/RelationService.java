package com.example.demo.services;

import com.example.demo.entities.UserProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelationServiceTest {

    private RelationService relationService;

    @BeforeEach
    void setUp() {
        relationService = new RelationService();
    }

    @Test
    void testGetProjectByUser() {
        // Get all projects for user ID 1
        List<UserProject> projects = relationService.getProjectByUser(10L);

        // Check that the list is not null
        assertNotNull(projects);

        // Print each project nicely
        for (UserProject up : projects) {
            System.out.println("User ID: " + up.getUser() + ", Project ID: " + up.getProject());
        }
    }

    @Test
    void testSetProjectUser() {
        // Example: insert a new relation
        UserProject up = new UserProject();
        up.setUser(1L);
        up.setProject(200L);

        boolean inserted = relationService.setProjectUser(up);
        assertTrue(inserted, "Should insert the relation successfully");

        System.out.println("Inserted relation: User ID " + up.getUser() + ", Project ID " + up.getProject());
    }

}
