package org.example.recipeapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeApplicationTest {

    @Test
    void contextLoads() {
        // This test just ensures the class can be loaded
        RecipeApplication app = new RecipeApplication();
        assertNotNull(app);
    }
}
