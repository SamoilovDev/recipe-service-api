package com.example.recipeservice.service;

import com.example.recipeservice.entity.RecipeEntity;
import com.example.recipeservice.model.RecipeDto;
import com.example.recipeservice.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    public RecipeEntity getRecipe() {
        if (recipeRepository.findAll().isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossible to find recipe");
        } else return recipeRepository.findAll().get(0);
    }

    public Map<String, String> postRecipe(RecipeDto recipeDto) {
        if (! recipeRepository.findAll().isEmpty()) {
            recipeRepository.delete(recipeRepository.findAll().get(0));
        }
        recipeRepository.save(new RecipeEntity(recipeDto));
        return Map.of("Success", "New recipe added");
    }

}
