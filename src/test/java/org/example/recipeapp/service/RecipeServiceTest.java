package org.example.recipeapp.service;

import org.example.recipeapp.dto.IngredientDto;
import org.example.recipeapp.dto.RecipeDto;
import org.example.recipeapp.model.Ingredient;
import org.example.recipeapp.model.Recipe;
import org.example.recipeapp.repo.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe recipe;
    private RecipeDto recipeDto;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pörkölt");
        recipe.setDescription("Finom magyar étel");
        recipe.setIngredients(new ArrayList<>());

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Hús");
        ingredient.setAmount("1kg");
        recipe.addIngredient(ingredient);

        IngredientDto ingredientDto = new IngredientDto(1L, "Hús", "1kg");
        recipeDto = new RecipeDto(1L, "Pörkölt", "Finom magyar étel", List.of(ingredientDto));
    }

    @Test
    void getAllRecipes_ShouldReturnList() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<RecipeDto> result = recipeService.getAllRecipes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pörkölt", result.get(0).name());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void createRecipe_ShouldReturnCreatedRecipe() {
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe savedRecipe = invocation.getArgument(0);
            savedRecipe.setId(1L);
            if (savedRecipe.getIngredients() != null) {
                long id = 1L;
                for (Ingredient i : savedRecipe.getIngredients()) {
                    i.setId(id++);
                }
            }
            return savedRecipe;
        });

        RecipeDto result = recipeService.createRecipe(recipeDto);

        assertNotNull(result);
        assertEquals("Pörkölt", result.name());
        assertEquals(1, result.ingredients().size());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void createRecipe_WithNullIngredients_ShouldReturnCreatedRecipe() {
        RecipeDto dtoWithoutIngredients = new RecipeDto(null, "Test", "Desc", null);
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe savedRecipe = invocation.getArgument(0);
            savedRecipe.setId(1L);
            return savedRecipe;
        });

        RecipeDto result = recipeService.createRecipe(dtoWithoutIngredients);

        assertNotNull(result);
        assertTrue(result.ingredients().isEmpty());
    }

    @Test
    void updateRecipe_ShouldReturnUpdatedRecipe() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeDto result = recipeService.updateRecipe(1L, recipeDto);

        assertNotNull(result);
        assertEquals("Pörkölt", result.name());
        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void updateRecipe_WithNullIngredients_ShouldReturnUpdatedRecipe() {
        RecipeDto dtoWithoutIngredients = new RecipeDto(1L, "Updated", "Updated Desc", null);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeDto result = recipeService.updateRecipe(1L, dtoWithoutIngredients);

        assertNotNull(result);
        assertTrue(result.ingredients().isEmpty());
    }

    @Test
    void updateRecipe_ShouldThrowException_WhenNotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> recipeService.updateRecipe(1L, recipeDto));
        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void deleteRecipe_ShouldCallRepository() {
        doNothing().when(recipeRepository).deleteById(1L);

        recipeService.deleteRecipe(1L);

        verify(recipeRepository, times(1)).deleteById(1L);
    }
}
