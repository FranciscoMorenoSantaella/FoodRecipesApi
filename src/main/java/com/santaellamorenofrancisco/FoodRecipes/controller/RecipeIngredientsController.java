package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeIngredientsNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeIngredientsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-ingredients")
@CrossOrigin(origins = "https://foodrecipesapi-production-07df.up.railway.app")
public class RecipeIngredientsController {

    @Autowired
    private RecipeIngredientsService recipeIngredientsService;

    @Operation(summary = "Obtener todas las relaciones RecipeIngredients")
    @GetMapping
    public ResponseEntity<List<RecipeIngredients>> getAllRecipeIngredients() {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsService.getAllRecipeIngredients();
        return ResponseEntity.ok(recipeIngredients);
    }

    @Operation(summary = "Obtener una relación RecipeIngredients por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relación RecipeIngredients encontrada"),
        @ApiResponse(responseCode = "404", description = "Relación RecipeIngredients no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredients> getRecipeIngredientsById(@PathVariable @Parameter(description = "ID de la relación RecipeIngredients") Long id) {
        try {
            RecipeIngredients recipeIngredients = recipeIngredientsService.getRecipeIngredientsById(id);
            return ResponseEntity.ok(recipeIngredients);
        } catch (RecipeIngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Guardar una lista de relaciones RecipeIngredients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Relaciones RecipeIngredients guardadas"),
        @ApiResponse(responseCode = "500", description = "Error interno al guardar las relaciones")
    })
    @PostMapping("/list")
    public ResponseEntity<List<RecipeIngredients>> saveRecipes(@RequestBody @Parameter(description = "Lista de relaciones RecipeIngredients") List<RecipeIngredients> recipeingredients) {
        try {
            List<RecipeIngredients> savedRecipes = recipeIngredientsService.saveAll(recipeingredients);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtener relaciones RecipeIngredients por receta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relaciones RecipeIngredients encontradas"),
        @ApiResponse(responseCode = "404", description = "No se encontraron relaciones para la receta")
    })
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeIngredients>> getRecipeIngredientsByRecipeId(@PathVariable @Parameter(description = "ID de la receta") Long recipeId) {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsService.getRecipeIngredientsByRecipeId(recipeId);
        return ResponseEntity.ok(recipeIngredients);
    }

    @Operation(summary = "Obtener relaciones RecipeIngredients por ingrediente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relaciones RecipeIngredients encontradas"),
        @ApiResponse(responseCode = "404", description = "No se encontraron relaciones para el ingrediente")
    })
    @GetMapping("/ingredient/{ingredientId}")
    public ResponseEntity<List<RecipeIngredients>> getRecipeIngredientsByIngredientId(@PathVariable @Parameter(description = "ID del ingrediente") Long ingredientId) {
        List<RecipeIngredients> recipeIngredients = recipeIngredientsService.getRecipeIngredientsByIngredientId(ingredientId);
        return ResponseEntity.ok(recipeIngredients);
    }

    @Operation(summary = "Crear o actualizar una relación RecipeIngredients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Relación RecipeIngredients guardada o actualizada"),
        @ApiResponse(responseCode = "500", description = "Error interno al guardar o actualizar la relación")
    })
    @PostMapping
    public ResponseEntity<RecipeIngredients> saveRecipeIngredients(@RequestBody @Parameter(description = "Relación RecipeIngredients") RecipeIngredients recipeIngredients) {
        try {
            RecipeIngredients savedRecipeIngredients = recipeIngredientsService.saveOrUpdateRecipeIngredients(recipeIngredients);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeIngredients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Eliminar una relación RecipeIngredients por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relación RecipeIngredients eliminada"),
        @ApiResponse(responseCode = "404", description = "Relación RecipeIngredients no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipeIngredients(@PathVariable @Parameter(description = "ID de la relación RecipeIngredients") Long id) {
        try {
            recipeIngredientsService.deleteRecipeIngredients(id);
            return ResponseEntity.ok("RecipeIngredients eliminado con éxito");
        } catch (RecipeIngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RecipeIngredients no encontrado");
        }
    }

    @ExceptionHandler(RecipeIngredientsNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(RecipeIngredientsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
