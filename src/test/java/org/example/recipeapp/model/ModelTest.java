package org.example.recipeapp.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void testRecipeAndIngredient() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setDescription("Test Description");

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");
        ingredient.setAmount("1 unit");

        recipe.addIngredient(ingredient);

        assertEquals(1L, recipe.getId());
        assertEquals("Test Recipe", recipe.getName());
        assertEquals("Test Description", recipe.getDescription());
        assertEquals(1, recipe.getIngredients().size());
        assertEquals(recipe, ingredient.getRecipe());
        assertEquals(1L, ingredient.getId());
        assertEquals("Test Ingredient", ingredient.getName());
        assertEquals("1 unit", ingredient.getAmount());
        
        recipe.setIngredients(new java.util.ArrayList<>());
        assertEquals(0, recipe.getIngredients().size());
    }
}
