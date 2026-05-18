package org.example.recipeapp.dto;

import java.util.List;

public record RecipeDto(Long id, String name, String description, List<IngredientDto> ingredients) {
}