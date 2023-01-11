package com.example.recipeservice.service;

import com.example.recipeservice.entity.RecipeEntity;
import com.example.recipeservice.model.RecipeDto;
import com.example.recipeservice.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    public RecipeDto getRecipe(Long id) {

        if (recipeRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossible to find recipe");
        }

        RecipeEntity recipe = recipeRepository.findById(id).get();
        return RecipeDto.builder()
                .name(recipe.getName())
                .category(recipe.getCategory())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients())
                .directions(recipe.getDirections())
                .build();
    }

    public Map<String, Long> postRecipe(RecipeDto recipeDto) {
        if (recipeRepository.findByName(recipeDto.getName()).isPresent()) {
            return Map.of("id", recipeRepository.save(
                            recipeRepository.findByName(recipeDto.getName())
                                    .get()
                                    .copyOf(recipeDto))
                    .getId()
            );
        } else {
            return Map.of("id", recipeRepository.save(new RecipeEntity().copyOf(recipeDto)).getId());
        }
    }

    public ResponseEntity<?>  updateRecipe(Long id, RecipeDto recipeDto) {
        if (recipeRepository.findById(id).isPresent()) {
            RecipeEntity recipe = recipeRepository.findById(id)
                    .get()
                    .copyOf(recipeDto);
            recipeRepository.save(recipe);

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Recipe with %d id doesn't exist".formatted(id));
    }

    public ResponseEntity<?> getRecipesByQuery(Map<String, String> query) {
        return ResponseEntity.ok().body(
                query.containsKey("category")
                        ? recipeRepository.findByCategoryIgnoreCaseOrderByPublishTimeDesc(query.get("category"))
                        : recipeRepository.findAllByNameLikeIgnoreCaseOrderByPublishTimeDesc(query.get("name"))
        );
    }

    public ResponseEntity<?> deleteRecipe(Long id) {
        if (recipeRepository.findById(id).isPresent()) {
            recipeRepository.delete(recipeRepository.findById(id).get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
