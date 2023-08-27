package com.example.recipeservice.controller;

import com.example.recipeservice.dto.RecipeIdDto;
import com.example.recipeservice.entity.UserEntity;
import com.example.recipeservice.dto.RecipeDto;
import com.example.recipeservice.dto.RegisterInfoDto;
import com.example.recipeservice.service.RecipeService;
import com.example.recipeservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecipeController {

    private final RecipeService recipeService;

    private final UserService userService;

    @GetMapping("/recipe/{id}")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable @Min(1) Long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping ("/recipe/new")
    @PreAuthorize(value = "isAuthenticated()")
    ResponseEntity<RecipeIdDto> postRecipe(
            @Valid @RequestBody RecipeDto recipe,
            @AuthenticationPrincipal UserEntity user) {
        return recipeService.postRecipe(recipe, user);
    }

    @PutMapping("/recipe/{id}")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<Object> updateRecipe(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody RecipeDto recipeDto,
            @AuthenticationPrincipal UserEntity user) {
        return recipeService.updateRecipe(id, recipeDto, user);
    }

    @GetMapping("/recipe/search")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<List<RecipeDto>> searchAndGetRecipe(
            @RequestParam("category") String category,
            @RequestParam("name") String name) {
        if (Objects.nonNull(category) || Objects.nonNull(name)) {
            return recipeService.getRecipesByQuery(category, name);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/recipe/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {
        return recipeService.deleteRecipe(id, user);
    }

    @PostMapping("/register")
    @PreAuthorize(value = "permitAll()")
    public ResponseEntity<RegisterInfoDto> createNewUser(@Valid @RequestBody RegisterInfoDto registerInfo) {
        return userService.createNewUser(registerInfo);
    }

}


