package com.example.recipeservice.service;

import com.example.recipeservice.entity.RecipeEntity;
import com.example.recipeservice.model.RecipeDto;
import com.example.recipeservice.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Map;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    public RecipeDto getRecipe(Long id) {

        if (recipeRepository.findById(id).isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossible to find recipe");
        }

        RecipeEntity recipe = recipeRepository.findById(id).get();

        return RecipeDto.builder()
                .name(recipe.getName())
                .description(recipe.getDescription())
                .ingredients(Arrays
                        .stream(recipe.getIngredients().split(", "))
                        .toList())
                .directions(Arrays
                        .stream(recipe.getDirections().split(", "))
                        .toList())
                .build();
    }

    public Map<String, Long> postRecipe(RecipeDto recipeDto) {
        recipeRepository.save(new RecipeEntity(recipeDto));
        return Map.of("id", recipeRepository
                .findByName(recipeDto.getName())
                .orElseThrow().getId());
    }

}
