package com.santaellamorenofrancisco.FoodRecipes.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

//Es una receta de comida
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "time", nullable = false)
    private int time;

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    @Column(name = "units", nullable = false)
    private String units;

    @Column(name = "image_url", nullable = true) // Campo para la URL de la imagen
    private String imageUrl;

    // Relación con categorías
    @JsonManagedReference
    @OneToMany(mappedBy = "recipe")
    private Set<RecipeCategory> recipeCategories = new HashSet<>();
    
    // Relación con ingredientes
    @JsonManagedReference
    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredients> recipeIngredients = new HashSet<>();

    // Lista de pasos de la receta
    @ElementCollection
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "step", nullable = true)  // Los pasos son opcionales
    private List<String> steps;

    // Constructor vacío
    public Recipe() {
    }
    
    // Constructor con ID
    public Recipe(Long id) {
        this.id = id;
    }

    // Constructor con parámetros
    public Recipe(String name, String description, int time, String difficulty, String units, List<String> steps, String imageUrl) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.difficulty = difficulty;
        this.units = units;
        this.steps = steps;
        this.imageUrl = imageUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Set<RecipeCategory> getRecipeCategories() {
        return recipeCategories;
    }

    public void setRecipeCategories(Set<RecipeCategory> recipeCategories) {
        this.recipeCategories = recipeCategories;
    }

    public Set<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(Set<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

	@Override
	public String toString() {
		return "Recipe [id=" + id + ", name=" + name + ", description=" + description + ", time=" + time
				+ ", difficulty=" + difficulty + ", units=" + units + ", imageUrl=" + imageUrl + ", recipeCategories="
				+ recipeCategories + ", recipeIngredients=" + recipeIngredients + ", steps=" + steps + "]";
	}
}
