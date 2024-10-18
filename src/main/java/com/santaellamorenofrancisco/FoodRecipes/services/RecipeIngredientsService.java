package com.santaellamorenofrancisco.FoodRecipes.services;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeIngredientsNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import com.santaellamorenofrancisco.FoodRecipes.repository.RecipeIngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeIngredientsService {

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    // Obtener todas las relaciones RecipeIngredients
    public List<RecipeIngredients> getAllRecipeIngredients() {
        return recipeIngredientsRepository.findAll();
    }

    // Obtener RecipeIngredients por ID
    public RecipeIngredients getRecipeIngredientsById(Long id) {
        return recipeIngredientsRepository.findById(id)
                .orElseThrow(() -> new RecipeIngredientsNotFoundException("RecipeIngredients con ID " + id + " no encontrado"));
    }

    // Obtener RecipeIngredients por receta
    public List<RecipeIngredients> getRecipeIngredientsByRecipeId(Long recipeId) {
        return recipeIngredientsRepository.findByRecipeId(recipeId);
    }

    // Obtener RecipeIngredients por ingrediente
    public List<RecipeIngredients> getRecipeIngredientsByIngredientId(Long ingredientId) {
        return recipeIngredientsRepository.findByIngredientId(ingredientId);
    }

    // Guardar o actualizar una relación RecipeIngredients
    public RecipeIngredients saveOrUpdateRecipeIngredients(RecipeIngredients recipeIngredients) {
    	System.out.println(recipeIngredients);
        return recipeIngredientsRepository.save(recipeIngredients);
    }
    
    // Método para guardar una lista de recipe ingredients
    public List<RecipeIngredients> saveAll(List<RecipeIngredients> recipeIngredients) {
        return recipeIngredientsRepository.saveAll(recipeIngredients); // Utiliza saveAll de JpaRepository
    }


    // Eliminar una relación RecipeIngredients por ID
    public void deleteRecipeIngredients(Long id) {
        RecipeIngredients recipeIngredients = getRecipeIngredientsById(id);
        recipeIngredientsRepository.delete(recipeIngredients);
    }
}
