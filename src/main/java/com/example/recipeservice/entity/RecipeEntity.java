package com.example.recipeservice.entity;

import com.example.recipeservice.model.RecipeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
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
        this.ingredients = String.join(", ", recipeDto.getIngredients());
        this.directions = String.join(", ", recipeDto.getDirections());
    }
}
