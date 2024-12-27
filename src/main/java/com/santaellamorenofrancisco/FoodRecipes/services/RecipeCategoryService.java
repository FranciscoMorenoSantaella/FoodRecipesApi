package com.santaellamorenofrancisco.FoodRecipes.services;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeCategoryNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import com.santaellamorenofrancisco.FoodRecipes.repository.RecipeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeCategoryService {

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    // Obtener todas las relaciones RecipeCategory
    public List<RecipeCategory> getAllRecipeCategories() {
        return recipeCategoryRepository.findAll();
    }

    // Obtener RecipeCategory por ID
    public RecipeCategory getRecipeCategoryById(Long id) {
        return recipeCategoryRepository.findById(id)
                .orElseThrow(() -> new RecipeCategoryNotFoundException("RecipeCategory con ID " + id + " no encontrado"));
    }

    // Obtener RecipeCategory por receta
    public List<RecipeCategory> getRecipeCategoriesByRecipeId(Long recipeId) {
        return recipeCategoryRepository.findByRecipeId(recipeId);
    }

    // Obtener RecipeCategory por categoría
    public List<RecipeCategory> getRecipeCategoriesByCategoryId(Long categoryId) {
        return recipeCategoryRepository.findByCategoryId(categoryId);
    }

    // Guardar o actualizar una relación RecipeCategory
    public RecipeCategory saveOrUpdateRecipeCategory(RecipeCategory recipeCategory) {
        return recipeCategoryRepository.save(recipeCategory);
    }

    // Eliminar una relación RecipeCategory por ID
    public void deleteRecipeCategory(Long id) {
        RecipeCategory recipeCategory = getRecipeCategoryById(id);
        recipeCategoryRepository.delete(recipeCategory);
    }
    
    public List<Recipe> getRecipesByCategoryName(String categoryName) {
        return recipeCategoryRepository.findRecipesByCategoryName(categoryName);
    }
    
    

    
    
}
