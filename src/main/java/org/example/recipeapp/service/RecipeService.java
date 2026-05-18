package org.example.recipeapp.service;

import org.example.recipeapp.dto.IngredientDto;
import org.example.recipeapp.dto.RecipeDto;
import org.example.recipeapp.model.Ingredient;
import org.example.recipeapp.model.Recipe;
import org.example.recipeapp.repo.RecipeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public RecipeDto createRecipe(RecipeDto dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.name());
        recipe.setDescription(dto.description());

        if (dto.ingredients() != null) {
            for (IngredientDto indDto : dto.ingredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(indDto.name());
                ingredient.setAmount(indDto.amount());
                recipe.addIngredient(ingredient);
            }
        }
        return mapToDto(recipeRepository.save(recipe));
    }

    public RecipeDto updateRecipe(Long id, RecipeDto dto) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.setName(dto.name());
        recipe.setDescription(dto.description());
        recipe.getIngredients().clear();

        if (dto.ingredients() != null) {
            for (IngredientDto indDto : dto.ingredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(indDto.name());
                ingredient.setAmount(indDto.amount());
                recipe.addIngredient(ingredient);
            }
        }
        return mapToDto(recipeRepository.save(recipe));
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    private RecipeDto mapToDto(Recipe recipe) {
        List<IngredientDto> ingDtos = recipe.getIngredients().stream()
                .map(i -> new IngredientDto(i.getId(), i.getName(), i.getAmount()))
                .collect(Collectors.toList());
        return new RecipeDto(recipe.getId(), recipe.getName(), recipe.getDescription(), ingDtos);
    }
}