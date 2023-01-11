package com.example.recipeservice.controller;

import com.example.recipeservice.model.RecipeDto;
import com.example.recipeservice.service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PostMapping ("/new")
    ResponseEntity<?> postRecipe(@Valid @RequestBody RecipeDto recipe) {
        return ResponseEntity.ok(recipeService.postRecipe(recipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable @Min(1) Long id,
                                          @Valid @RequestBody RecipeDto recipeDto) {
        return recipeService.updateRecipe(id, recipeDto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAndGetRecipe(@RequestParam Map<String, String> searchQuery) {
        if (searchQuery.keySet().size() == 1 &&
                (searchQuery.containsKey("category") || searchQuery.containsKey("name"))) {
            return recipeService.getRecipesByQuery(searchQuery);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        return recipeService.deleteRecipe(id);
    }

}

