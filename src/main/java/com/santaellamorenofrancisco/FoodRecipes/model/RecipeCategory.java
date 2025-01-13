	package com.santaellamorenofrancisco.FoodRecipes.model;
	
	import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
	
	//Es la relación que tiene la receta y las categorías a las que pertenece
	@Entity
	@Table(name = "recipe_category") // Nombre de la tabla intermedia
	public class RecipeCategory {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id; // Clave primaria
	    
	    @ManyToOne
	    @JsonBackReference
	    @JoinColumn(name = "recipe_id", nullable = false) // Clave foránea hacia Recipe
	    private Recipe recipe;
	
	    @ManyToOne
	    @JoinColumn(name = "category_id", nullable = false) // Clave foránea 1hacia Category
	    private Category category;
	
	
	    // Constructor vacío
	    public RecipeCategory() {
	    }
	
	    // Constructor con parámetros
	    public RecipeCategory(Recipe recipe, Category category) {
	        this.recipe = recipe;
	        this.category = category;
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
	
	    public Category getCategory() {
	        return category;
	    }
	
	    public void setCategory(Category category) {
	        this.category = category;
	    }
	
		@Override
		public String toString() {
			return "RecipeCategory [id=" + id + ", recipe=" + recipe + ", category=" + category + "]";
		}
	}
