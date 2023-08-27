package com.example.recipeservice.entity;

import com.example.recipeservice.dto.RecipeDto;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Embeddable
@Table(name = "recipe")
public class RecipeEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @UpdateTimestamp
    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishTime;

    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "ingredients",
            joinColumns = {
                    @JoinColumn(name = "ingredients_id")
            }
    )
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(
            name = "directions",
            joinColumns = {
                    @JoinColumn(name = "directions_id")
            }
    )
    private List<String> directions;

    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity user;

    public RecipeEntity copyOf(RecipeDto recipeDto) {
        this.name = recipeDto.getName();
        this.category = recipeDto.getCategory();
        this.description = recipeDto.getDescription();
        this.ingredients = recipeDto.getIngredients();
        this.directions = recipeDto.getDirections();
        return this;
    }

}
