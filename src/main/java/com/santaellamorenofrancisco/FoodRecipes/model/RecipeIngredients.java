package com.santaellamorenofrancisco.FoodRecipes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

//Es la relacion entre las recetas y los ingredientes que tiene
@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonProperty("recipe")
    @JsonBackReference
    @JoinColumn(name = "recipe_id", nullable = false) // Clave foránea hacia Recipe
    private Recipe recipe;

    @ManyToOne
    //@JsonIgnore
    @JsonProperty("ingredient")
    @JoinColumn(name = "ingredient_id", nullable = false) // Clave foránea hacia Ingredients
    private Ingredients ingredient;

    @Column(name = "quantity", nullable = false) // Atributo adicional para la cantidad
    private String quantity;
    
    @Column(name = "type_quantity", nullable = false)
    private String type_quantity;

    // Constructor vacío
    public RecipeIngredients() {
    }

  
    public RecipeIngredients(Recipe recipe, Ingredients ingredient, String quantity, String type_quantity) {
		this.recipe = recipe;
		this.ingredient = ingredient;
		this.quantity = quantity;
		this.type_quantity = type_quantity;
	}


	// Getters y Setters	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredients getIngredients() {
        return ingredient;
    }

    public void setIngredients(Ingredients ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    
	public Ingredients getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredients ingredient) {
		this.ingredient = ingredient;
	}


	public String getType_quantity() {
		return type_quantity;
	}


	public void setType_quantity(String type_quantity) {
		this.type_quantity = type_quantity;
	}


	@Override
	public String toString() {
		return "RecipeIngredients [id=" + id + ", recipe=" + recipe + ", ingredient=" + ingredient + ", quantity="
				+ quantity + ", type_quantity=" + type_quantity + "]";
	}
   
}
