package com.example.recipeservice.controller;

import com.example.recipeservice.model.RecipeDto;
import com.example.recipeservice.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @GetMapping
    public ResponseEntity<?> getRecipe() {
        return ResponseEntity.ok(recipeService.getRecipe());
    }

    @PostMapping ResponseEntity<?> postRecipe(@Valid @RequestBody RecipeDto recipe) {
        return ResponseEntity.ok(recipeService.postRecipe(recipe));
    }
}
