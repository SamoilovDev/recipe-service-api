package com.example.recipeservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeDto {

    @NotBlank(message = "name of recipe is required")
    private String name;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "ingredients are required")
    private List<String> ingredients;

    @NotNull(message = "directions are required")
    private List<String> directions;
}
