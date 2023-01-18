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
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;

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

    public Map<String, Long> postRecipe(RecipeDto recipeDto, UserEntity user) {
        if (recipeRepository.findByName(recipeDto.getName()).isPresent()) {
            user.getRecipes().add(
                    recipeRepository.findByName(recipeDto.getName())
                            .get()
                            .copyOf(recipeDto)
                            .setUser(user)
            );
        } else {
            user.getRecipes().add(new RecipeEntity().copyOf(recipeDto).setUser(user));
        }
        userRepository.save(user);
        return Map.of("id",
                recipeRepository.findByName(recipeDto.getName()).get().getId()
        );
    }

    public ResponseEntity<?> updateRecipe(Long id, RecipeDto recipeDto, UserEntity user) {
        if (recipeRepository.findById(id).isPresent()) {
            if (! user.getRecipes().contains(recipeRepository.findById(id).get())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you can not update with recipe!");
            }

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

    public ResponseEntity<?> deleteRecipe(Long id, UserEntity user) {
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()
                && user.getRecipes().contains(optionalRecipe.get())) {
            recipeRepository.delete(optionalRecipe.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else if (optionalRecipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe with this id doesn't exist");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you can not delete this recipe!");
        }
    }

}
