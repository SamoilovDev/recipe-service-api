package com.example.recipeservice.mapper;

import com.example.recipeservice.dto.RecipeDto;
import com.example.recipeservice.entity.RecipeEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {

    public RecipeDto mapRecipeEntityToRecipeDto(RecipeEntity recipeEntity) {
        return RecipeDto
                .builder()
                .name(recipeEntity.getName())
                .category(recipeEntity.getCategory())
                .description(recipeEntity.getDescription())
                .ingredients(recipeEntity.getIngredients())
                .directions(recipeEntity.getDirections())
                .publishTime(recipeEntity.getPublishTime())
                .build();
    }

    public RecipeEntity mapRecipeDtoToRecipeEntity(RecipeDto recipeDto) {
        return RecipeEntity
                .builder()
                .name(recipeDto.getName())
                .description(recipeDto.getDescription())
                .category(recipeDto.getCategory())
                .publishTime(recipeDto.getPublishTime())
                .ingredients(recipeDto.getIngredients())
                .directions(recipeDto.getDirections())
                .build();
    }


}
