package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.IngredientsNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Ingredients;
import com.santaellamorenofrancisco.FoodRecipes.services.IngredientsService;
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
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "https://foodrecipesapi-production-07df.up.railway.app")
public class IngredientsController {

    @Autowired
    private IngredientsService ingredientsService;

    @Operation(summary = "Obtiene todos los ingredientes")
    @ApiResponse(responseCode = "200", description = "Lista de ingredientes obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class)))
    @GetMapping
    public ResponseEntity<List<Ingredients>> getAllIngredients() {
        List<Ingredients> ingredients = ingredientsService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @Operation(summary = "Obtiene un ingrediente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingrediente encontrado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))),
        @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado", 
                     content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Ingredients> getIngredientById(
            @Parameter(description = "ID del ingrediente") @PathVariable Long id) {
        try {
            Ingredients ingredient = ingredientsService.getIngredientById(id);
            return ResponseEntity.ok(ingredient);
        } catch (IngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Obtiene un ingrediente por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingrediente encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))),
        @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado", 
                     content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Ingredients> getIngredientByName(
            @Parameter(description = "Nombre del ingrediente") @PathVariable String name) {
        try {
            Ingredients ingredient = ingredientsService.getIngredientByName(name);
            return ResponseEntity.ok(ingredient);
        } catch (IngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Crea o actualiza un ingrediente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ingrediente creado o actualizado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))),
        @ApiResponse(responseCode = "500", description = "Error al crear o actualizar el ingrediente",
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Ingredients> saveIngredient(
            @Parameter(description = "Datos del ingrediente a crear o actualizar") @RequestBody Ingredients ingredient) {
        try {
            Ingredients savedIngredient = ingredientsService.saveOrUpdateIngredient(ingredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Guarda una lista de ingredientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ingredientes creados exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ingredients.class))),
        @ApiResponse(responseCode = "500", description = "Error al guardar los ingredientes",
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/list")
    public ResponseEntity<List<Ingredients>> saveRecipes(
            @Parameter(description = "Lista de ingredientes a guardar") @RequestBody List<Ingredients> ingredients) {
        try {
            List<Ingredients> savedRecipes = ingredientsService.saveAll(ingredients);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Elimina un ingrediente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingrediente eliminado exitosamente", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error al eliminar el ingrediente",
                     content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredient(
            @Parameter(description = "ID del ingrediente a eliminar") @PathVariable Long id) {
        try {
            ingredientsService.deleteIngredient(id);
            return ResponseEntity.ok("Ingrediente eliminado con éxito");
        } catch (IngredientsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingrediente no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el ingrediente");
        }
    }
}
