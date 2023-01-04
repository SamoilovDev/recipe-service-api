package com.example.recipeservice.entity;

import com.example.recipeservice.model.RecipeDto;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@ToString @Builder
@RequiredArgsConstructor
@Entity
@Table(name = "recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "ingredients", nullable = false)
    private String ingredients;

    @Column(name = "directions", nullable = false)
    private String directions;

    public RecipeEntity(RecipeDto recipeDto) {
        this.name = recipeDto.getName();
        this.description = recipeDto.getDescription();
        this.ingredients = recipeDto.getIngredients();
        this.directions = recipeDto.getDirections();
    }
}
