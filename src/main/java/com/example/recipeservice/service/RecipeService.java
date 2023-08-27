package com.example.recipeservice.service;

import com.example.recipeservice.dto.RecipeIdDto;
import com.example.recipeservice.entity.RecipeEntity;
import com.example.recipeservice.entity.UserEntity;
import com.example.recipeservice.dto.RecipeDto;
import com.example.recipeservice.mapper.RecipeMapper;
import com.example.recipeservice.repository.RecipeRepository;
import com.example.recipeservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private final RecipeMapper recipeMapper;

    public static final String RECIPE_DOES_NOT_EXIST = "Recipe with this id doesn't exist";
    public static final String IMPOSSIBLE_TO_DELETE_RECIPE = "You can not delete this recipe!";
    public static final String IMPOSSIBLE_TO_UPDATE_RECIPE = "You can not update this recipe!";

    public ResponseEntity<RecipeDto> getRecipe(Long id) {
        RecipeEntity recipe = recipeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, RECIPE_DOES_NOT_EXIST));
        return ResponseEntity.ok(recipeMapper.mapRecipeEntityToRecipeDto(recipe));
    }

    public ResponseEntity<RecipeIdDto> postRecipe(RecipeDto recipeDto, UserEntity user) {
        RecipeEntity recipe = recipeRepository
                .findByName(recipeDto.getName())
                .orElse(new RecipeEntity())
                .copyOf(recipeDto);

        if (Objects.equals(null, recipe.getUser())) {
            recipe.setUser(user);
        }
        recipeRepository.save(recipe);

        RecipeIdDto recipeIdDto = RecipeIdDto
                .builder()
                .id(recipeRepository.findByName(recipeDto.getName()).orElseThrow().getId())
                .build();

        return ResponseEntity.ok(recipeIdDto);
    }

    @Transactional
    public ResponseEntity<Object> updateRecipe(Long id, RecipeDto recipeDto, UserEntity user) {
        RecipeEntity foundedRecipe = recipeRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Recipe with %d id doesn't exist".formatted(id)
                        )
                )
                .copyOf(recipeDto);

        if (Objects.equals(foundedRecipe.getUser(), user) ) {
            recipeRepository.save(foundedRecipe);
            user.getRecipes().add(foundedRecipe);
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, IMPOSSIBLE_TO_UPDATE_RECIPE);
        }
    }

    public ResponseEntity<List<RecipeDto>> getRecipesByQuery(String category, String name) {
        if (Objects.nonNull(category) && Objects.nonNull(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            List<RecipeEntity> recipeEntities = Objects.nonNull(category)
                    ? recipeRepository.findByCategoryIgnoreCaseOrderByPublishTimeDesc(category)
                    : recipeRepository.findAllByNameLikeIgnoreCaseOrderByPublishTimeDesc(name);

            return ResponseEntity
                    .ok()
                    .body(
                            recipeEntities
                                    .stream()
                                    .map(recipeMapper::mapRecipeEntityToRecipeDto)
                                    .toList()
                    );
        }
    }

    public ResponseEntity<Object> deleteRecipe(Long id, UserEntity user) {
        RecipeEntity foundedRecipe = recipeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, RECIPE_DOES_NOT_EXIST));

        if (user.getRecipes().contains(foundedRecipe)) {
            recipeRepository.delete(foundedRecipe);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }  else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, IMPOSSIBLE_TO_DELETE_RECIPE);
        }
    }

}
