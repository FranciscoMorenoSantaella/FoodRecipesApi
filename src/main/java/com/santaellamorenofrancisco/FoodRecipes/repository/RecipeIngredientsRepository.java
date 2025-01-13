package com.santaellamorenofrancisco.FoodRecipes.repository;

import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Long> {

    // Consultas personalizadas
	
	// Busca los ingrediente de una receta segun su id
    List<RecipeIngredients> findByRecipeId(Long recipeId);
    
    // Busca las recetas que tengan un ingrediente especifico segun su id
    List<RecipeIngredients> findByIngredientId(Long ingredientId);
}
