package com.example.recipeservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import com.example.recipeservice.entity.UserEntity;
import com.example.recipeservice.model.RecipeDto;
import com.example.recipeservice.model.RegisterInfoDto;
import com.example.recipeservice.service.RecipeService;
import com.example.recipeservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @GetMapping("/recipe/{id}")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<?> getRecipe(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PostMapping ("/recipe/new")
    @PreAuthorize(value = "isAuthenticated()")
    ResponseEntity<?> postRecipe(@Valid @RequestBody RecipeDto recipe,
                                 @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(recipeService.postRecipe(recipe, user));
    }

    @PutMapping("/recipe/{id}")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<?> updateRecipe(@PathVariable @Min(1) Long id,
                                          @Valid @RequestBody RecipeDto recipeDto,
                                          @AuthenticationPrincipal UserEntity user) {
        return recipeService.updateRecipe(id, recipeDto, user);
    }

    @GetMapping("/recipe/search")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<?> searchAndGetRecipe(@RequestParam Map<String, String> searchQuery) {
        if (searchQuery.keySet().size() == 1 &&
                (searchQuery.containsKey("category") || searchQuery.containsKey("name"))) {
            return recipeService.getRecipesByQuery(searchQuery);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
    }

    @DeleteMapping("/recipe/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id,
                                          @AuthenticationPrincipal UserEntity user) {
        return recipeService.deleteRecipe(id, user);
    }

    @PostMapping("/register")
    @PreAuthorize(value = "permitAll()")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegisterInfoDto registerInfo) {
        return userService.createNewUser(registerInfo);
    }

}

