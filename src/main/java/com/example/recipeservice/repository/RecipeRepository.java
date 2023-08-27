package com.example.recipeservice.repository;

import com.example.recipeservice.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    Optional<RecipeEntity> findByName(String name);

    List<RecipeEntity> findByCategoryIgnoreCaseOrderByPublishTimeDesc(String category);

    List<RecipeEntity> findAllByNameLikeIgnoreCaseOrderByPublishTimeDesc(String name);

}
