package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.IngredientsNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Ingredients;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.services.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientsController {

    @Autowired
    private IngredientsService ingredientsService;

    // Obtener todos los ingredientes
    @GetMapping
    public ResponseEntity<List<Ingredients>> getAllIngredients() {
        List<Ingredients> ingredients = ingredientsService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    // Obtener ingrediente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Ingredients> getIngredientById(@PathVariable Long id) {
        try {
            Ingredients ingredient = ingredientsService.getIngredientById(id);
            return ResponseEntity.ok(ingredient);
        } catch (IngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentra
        }
    }

    // Obtener ingrediente por nombre
    @GetMapping("/name/{name}")
    public ResponseEntity<Ingredients> getIngredientByName(@PathVariable String name) {
        try {
            Ingredients ingredient = ingredientsService.getIngredientByName(name);
            return ResponseEntity.ok(ingredient);
        } catch (IngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentra
        }
    }

    // Crear o actualizar ingrediente
    @PostMapping
    public ResponseEntity<Ingredients> saveIngredient(@RequestBody Ingredients ingredient) {
        try {
            Ingredients savedIngredient = ingredientsService.saveOrUpdateIngredient(ingredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient); // 201 si se crea con éxito
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 si hay error
        }
    }
    
    @PostMapping("/list")
    public ResponseEntity<List<Ingredients>> saveRecipes(@RequestBody List<Ingredients> ingredients) {
        try {
            List<Ingredients> savedRecipes = ingredientsService.saveAll(ingredients);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Eliminar ingrediente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable Long id) {
        try {
            ingredientsService.deleteIngredient(id);
            return ResponseEntity.ok("Ingrediente eliminado con éxito");
        } catch (IngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingrediente no encontrado"); // 404 si no se encuentra
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el ingrediente"); // 500 si hay error
        }
    }

    // Manejo global de excepciones de ingrediente no encontrado
    @ExceptionHandler(IngredientsNotFoundException.class)
    public ResponseEntity<String> handleIngredientNotFoundException(IngredientsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
