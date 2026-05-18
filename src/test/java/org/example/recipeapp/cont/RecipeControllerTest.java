package org.example.recipeapp.cont;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.recipeapp.dto.RecipeDto;
import org.example.recipeapp.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    private RecipeDto recipeDto;

    @BeforeEach
    void setUp() {
        recipeDto = new RecipeDto(1L, "Pörkölt", "Desc", List.of());
    }

    @Test
    void getAll_ShouldReturnList() throws Exception {
        when(recipeService.getAllRecipes()).thenReturn(List.of(recipeDto));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pörkölt"));
    }

    @Test
    void create_ShouldReturnCreated() throws Exception {
        when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(recipeDto);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pörkölt"));
    }

    @Test
    void update_ShouldReturnOk() throws Exception {
        when(recipeService.updateRecipe(eq(1L), any(RecipeDto.class))).thenReturn(recipeDto);

        mockMvc.perform(put("/api/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pörkölt"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(recipeService).deleteRecipe(1L);

        mockMvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isNoContent());
    }
}
