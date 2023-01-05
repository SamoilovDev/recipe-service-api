package com.example.recipeservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeDto {

    @NotBlank(message = "name of recipe is required")
    private String name;

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "ingredients are required")
    private String ingredients;

    @NotBlank(message = "directions are required")
    private String directions;
}
