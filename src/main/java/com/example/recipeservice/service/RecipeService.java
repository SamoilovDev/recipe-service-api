package com.example.recipeservice.service;

import com.example.recipeservice.entity.RecipeEntity;
import com.example.recipeservice.entity.UserEntity;
import com.example.recipeservice.model.RecipeDto;
import com.example.recipeservice.repository.RecipeRepository;
import com.example.recipeservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<RecipeDto> getRecipe(Long id) {
        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossible to find recipe")
                );
        RecipeDto recipeDto = RecipeDto.builder()
                .name(recipe.getName())
                .category(recipe.getCategory())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients())
                .directions(recipe.getDirections())
                .build();

        return ResponseEntity.ok(recipeDto);
    }

    public ResponseEntity<Map<String, Long>> postRecipe(RecipeDto recipeDto, UserEntity user) {
        RecipeEntity recipe = recipeRepository.findByName(recipeDto.getName())
                .orElse(new RecipeEntity())
                .copyOf(recipeDto);

        if (Objects.equals(null, recipe.getUser())) recipe.setUser(user);
        recipeRepository.save(recipe);

        return ResponseEntity.ok(
                Map.of(
                        "id",
                        recipeRepository.findByName(recipeDto.getName()).orElseThrow().getId()
                )
        );
    }

    public ResponseEntity<?> updateRecipe(Long id, RecipeDto recipeDto, UserEntity user) {
        RecipeEntity foundedRecipe = recipeRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Recipe with %d id doesn't exist".formatted(id)
                        )
                )
                .copyOf(recipeDto);

        if (Objects.equals(foundedRecipe.getUser(), user) ) {
            recipeRepository.save(foundedRecipe);
            user.getRecipes().add(foundedRecipe);
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not update this recipe!");

    }

    public ResponseEntity<?> getRecipesByQuery(Map<String, String> query) {
        return ResponseEntity.ok().body(
                query.containsKey("category")
                        ? recipeRepository.findByCategoryIgnoreCaseOrderByPublishTimeDesc(query.get("category"))
                        : recipeRepository.findAllByNameLikeIgnoreCaseOrderByPublishTimeDesc(query.get("name"))
        );
    }

    public ResponseEntity<?> deleteRecipe(Long id, UserEntity user) {
        RecipeEntity foundedRecipe = recipeRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with this id doesn't exist")
                );

        if (user.getRecipes().contains(foundedRecipe)) {
            recipeRepository.delete(foundedRecipe);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }  else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not delete this recipe!");

    }

}
