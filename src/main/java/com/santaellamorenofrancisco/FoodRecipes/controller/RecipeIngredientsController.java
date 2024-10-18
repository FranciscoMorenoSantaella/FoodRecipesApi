package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeIngredientsNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-ingredients")
public class RecipeIngredientsController {

    @Autowired
    private RecipeIngredientsService recipeIngredientsService;

    // Obtener todas las relaciones RecipeIngredients
    @GetMapping
    public ResponseEntity<List<RecipeIngredients>> getAllRecipeIngredients() {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsService.getAllRecipeIngredients();
        return ResponseEntity.ok(recipeIngredients);
    }

    // Obtener una relación RecipeIngredients por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredients> getRecipeIngredientsById(@PathVariable Long id) {
        try {
            RecipeIngredients recipeIngredients = recipeIngredientsService.getRecipeIngredientsById(id);
            return ResponseEntity.ok(recipeIngredients);
        } catch (RecipeIngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentra
        }
    }
    
    @PostMapping("/list")
    public ResponseEntity<List<RecipeIngredients>> saveRecipes(@RequestBody List<RecipeIngredients> recipeingredients) {
        try {
            List<RecipeIngredients> savedRecipes = recipeIngredientsService.saveAll(recipeingredients);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Obtener relaciones RecipeIngredients por receta
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeIngredients>> getRecipeIngredientsByRecipeId(@PathVariable Long recipeId) {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsService.getRecipeIngredientsByRecipeId(recipeId);
        return ResponseEntity.ok(recipeIngredients);
    }

    // Obtener relaciones RecipeIngredients por ingrediente
    @GetMapping("/ingredient/{ingredientId}")
    public ResponseEntity<List<RecipeIngredients>> getRecipeIngredientsByIngredientId(@PathVariable Long ingredientId) {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsService.getRecipeIngredientsByIngredientId(ingredientId);
        return ResponseEntity.ok(recipeIngredients);
    }

    // Crear o actualizar una relación RecipeIngredients
    @PostMapping
    public ResponseEntity<RecipeIngredients> saveRecipeIngredients(@RequestBody RecipeIngredients recipeIngredients) {
    	System.out.println("hola?");
    	System.out.println(recipeIngredients);
        RecipeIngredients savedRecipeIngredients = recipeIngredientsService.saveOrUpdateRecipeIngredients(recipeIngredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeIngredients);
    }

    // Eliminar una relación RecipeIngredients por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipeIngredients(@PathVariable Long id) {
        try {
            recipeIngredientsService.deleteRecipeIngredients(id);
            return ResponseEntity.ok("RecipeIngredients eliminado con éxito");
        } catch (RecipeIngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RecipeIngredients no encontrado");
        }
    }

    // Manejo global de excepciones de RecipeIngredientsNotFoundException
    @ExceptionHandler(RecipeIngredientsNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(RecipeIngredientsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
