package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeCategoryNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-categories")
@CrossOrigin(origins = "https://foodrecipesapi-production-07df.up.railway.app")
public class RecipeCategoryController {

    @Autowired
    private RecipeCategoryService recipeCategoryService;

    @Operation(summary = "Obtiene todas las categorías de recetas")
    @ApiResponse(responseCode = "200", description = "Lista de categorías de recetas obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategory.class)))
    @GetMapping
    public ResponseEntity<List<RecipeCategory>> getAllRecipeCategories() {
        List<RecipeCategory> recipeCategories = recipeCategoryService.getAllRecipeCategories();
        return ResponseEntity.ok(recipeCategories);
    }

    @Operation(summary = "Obtiene una categoría de receta por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría de receta encontrada", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategory.class))),
        @ApiResponse(responseCode = "404", description = "Categoría de receta no encontrada", 
                     content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeCategory> getRecipeCategoryById(
            @Parameter(description = "ID de la categoría de receta") @PathVariable Long id) {
        try {
            RecipeCategory recipeCategory = recipeCategoryService.getRecipeCategoryById(id);
            return ResponseEntity.ok(recipeCategory);
        } catch (RecipeCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Obtiene las categorías de receta por ID de receta")
    @ApiResponse(responseCode = "200", description = "Categorías de receta obtenidas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategory.class)))
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeCategory>> getRecipeCategoriesByRecipeId(
            @Parameter(description = "ID de la receta") @PathVariable Long recipeId) {
        List<RecipeCategory> recipeCategories = recipeCategoryService.getRecipeCategoriesByRecipeId(recipeId);
        return ResponseEntity.ok(recipeCategories);
    }

    @Operation(summary = "Obtiene las categorías de receta por ID de categoría")
    @ApiResponse(responseCode = "200", description = "Categorías de receta obtenidas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategory.class)))
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<RecipeCategory>> getRecipeCategoriesByCategoryId(
            @Parameter(description = "ID de la categoría") @PathVariable Long categoryId) {
        List<RecipeCategory> recipeCategories = recipeCategoryService.getRecipeCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(recipeCategories);
    }

    @Operation(summary = "Crea o actualiza una categoría de receta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría de receta creada o actualizada exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategory.class))),
        @ApiResponse(responseCode = "500", description = "Error al crear o actualizar la categoría de receta",
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<RecipeCategory> saveRecipeCategory(
            @Parameter(description = "Datos de la categoría de receta a crear o actualizar") @RequestBody RecipeCategory recipeCategory) {
        RecipeCategory savedRecipeCategory = recipeCategoryService.saveOrUpdateRecipeCategory(recipeCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeCategory);
    }

    @Operation(summary = "Elimina una categoría de receta por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría de receta eliminada exitosamente",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Categoría de receta no encontrada",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error al eliminar la categoría de receta",
                     content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipeCategory(
            @Parameter(description = "ID de la categoría de receta a eliminar") @PathVariable Long id) {
        try {
            recipeCategoryService.deleteRecipeCategory(id);
            return ResponseEntity.ok("Categoría de receta eliminada con éxito");
        } catch (RecipeCategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría de receta no encontrada");
        }
    }

    @ExceptionHandler(RecipeCategoryNotFoundException.class)
    public ResponseEntity<String> handleRecipeCategoryNotFoundException(RecipeCategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
