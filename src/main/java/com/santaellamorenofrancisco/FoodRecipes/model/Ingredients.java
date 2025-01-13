package com.santaellamorenofrancisco.FoodRecipes.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

//Son los ingredientes que tendran la receta
@Entity
@Table(name = "ingredients")
public class Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "alergenos", nullable = true)
    private String alergenos;

    // Relación con RecipeIngredient
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecipeIngredients> recipeIngredients = new HashSet<>();
    
    // Constructor vacío
    public Ingredients() {
    }

    // Constructor con ID
    public Ingredients(Long id) {
        this.id = id;
    }

    // Constructor con parámetros
    public Ingredients(String name, String alergenos) {
        this.alergenos = alergenos;
        this.name = name;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(String alergenos) {
        this.alergenos = alergenos;
    }

    public Set<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(Set<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

	@Override
	public String toString() {
		return "Ingredients [id=" + id + ", name=" + name + ", alergenos=" + alergenos + ", recipeIngredients="
				+ recipeIngredients + "]";
	}
    
    
}
