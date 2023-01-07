package com.example.recipeservice.repository;

import com.example.recipeservice.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Optional<RecipeEntity> findById(Long id);

    Optional<RecipeEntity> findByName(String name);
}
