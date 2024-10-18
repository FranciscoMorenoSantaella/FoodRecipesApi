package com.santaellamorenofrancisco.FoodRecipes.repository;

import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Long> {

    // Consultas personalizadas
    List<RecipeIngredients> findByRecipeId(Long recipeId);

    List<RecipeIngredients> findByIngredientId(Long ingredientId);
}
