package com.example.recipeservice.entity;

import com.example.recipeservice.model.RecipeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @UpdateTimestamp
    private LocalDateTime publishTime;

    @Column(name = "description")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ingredients",
    joinColumns = @JoinColumn(name = "ingredients_id"))
    private List<String> ingredients;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "directions",
    joinColumns = @JoinColumn(name = "directions_id"))
    private List<String> directions;

    public RecipeEntity copyOf(RecipeDto recipeDto) {
        this.name = recipeDto.getName();
        this.category = recipeDto.getCategory();
        this.description = recipeDto.getDescription();
        this.ingredients = recipeDto.getIngredients();
        this.directions = recipeDto.getDirections();
        return this;
    }
}
