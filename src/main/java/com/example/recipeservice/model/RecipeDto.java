package com.example.recipeservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RecipeDto {

    @NotBlank(message = "name of recipe is required")
    private String name;

    @NotBlank(message = "category of recipe is required")
    private String category;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "date")
    @Builder.Default
    private LocalDateTime publishTime = LocalDateTime.now();

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "ingredients are required")
    @Size(min = 1, message = "Recipe must contain at least 1 ingredient")
    private List<String> ingredients;

    @NotNull(message = "directions are required")
    @Size(min = 1, message = "Recipe must contain at least 1 direction")
    private List<String> directions;

}
