package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeCategoryNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-categories")
public class RecipeCategoryController {

    @Autowired
    private RecipeCategoryService recipeCategoryService;

    // Obtener todas las relaciones RecipeCategory
    @GetMapping
    public ResponseEntity<List<RecipeCategory>> getAllRecipeCategories() {
        List<RecipeCategory> recipeCategories = recipeCategoryService.getAllRecipeCategories();
        return ResponseEntity.ok(recipeCategories);
    }

    // Obtener una relación RecipeCategory por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecipeCategory> getRecipeCategoryById(@PathVariable Long id) {
        try {
            RecipeCategory recipeCategory = recipeCategoryService.getRecipeCategoryById(id);
            return ResponseEntity.ok(recipeCategory);
        } catch (RecipeCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentra
        }
    }

    // Obtener relaciones RecipeCategory por receta
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeCategory>> getRecipeCategoriesByRecipeId(@PathVariable Long recipeId) {
        List<RecipeCategory> recipeCategories = recipeCategoryService.getRecipeCategoriesByRecipeId(recipeId);
        return ResponseEntity.ok(recipeCategories);
    }

    // Obtener relaciones RecipeCategory por categoría
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<RecipeCategory>> getRecipeCategoriesByCategoryId(@PathVariable Long categoryId) {
        List<RecipeCategory> recipeCategories = recipeCategoryService.getRecipeCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(recipeCategories);
    }

    // Crear o actualizar una relación RecipeCategory
    @PostMapping
    public ResponseEntity<RecipeCategory> saveRecipeCategory(@RequestBody RecipeCategory recipeCategory) {
        RecipeCategory savedRecipeCategory = recipeCategoryService.saveOrUpdateRecipeCategory(recipeCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeCategory);
    }

    // Eliminar una relación RecipeCategory por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipeCategory(@PathVariable Long id) {
        try {
            recipeCategoryService.deleteRecipeCategory(id);
            return ResponseEntity.ok("RecipeCategory eliminado con éxito");
        } catch (RecipeCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RecipeCategory no encontrado");
        }
    }

    // Manejo global de excepciones de RecipeCategoryNotFoundException
    @ExceptionHandler(RecipeCategoryNotFoundException.class)
    public ResponseEntity<String> handleRecipeCategoryNotFoundException(RecipeCategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
