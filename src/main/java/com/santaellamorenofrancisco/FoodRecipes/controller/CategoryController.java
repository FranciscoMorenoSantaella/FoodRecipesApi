package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.CategoryNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Category;
import com.santaellamorenofrancisco.FoodRecipes.services.CategoryService;
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
@RequestMapping("/api/categories")
@CrossOrigin(origins = "https://foodrecipesapi-production-07df.up.railway.app")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida con éxito",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Category.class)))
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría específica dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID de la categoría a buscar", required = true)
            @PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Obtener categoría por nombre", description = "Devuelve una categoría específica dado su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(
            @Parameter(description = "Nombre de la categoría a buscar", required = true)
            @PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(category);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Crear o actualizar una categoría", description = "Guarda o actualiza una categoría. Devuelve la categoría creada o actualizada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada o actualizada con éxito",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "500", description = "Error al crear o actualizar la categoría")
    })
    @PostMapping
    public ResponseEntity<Category> saveCategory(
            @Parameter(description = "Datos de la categoría a crear o actualizar", required = true)
            @RequestBody Category category) {
        try {
            Category savedCategory = categoryService.saveOrUpdateCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Crear o actualizar una lista de categorías", description = "Guarda o actualiza una lista de categorías.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lista de categorías creada o actualizada con éxito",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "500", description = "Error al crear o actualizar la lista de categorías")
    })
    @PostMapping("/list")
    public ResponseEntity<List<Category>> saveRecipes(
            @Parameter(description = "Lista de categorías a crear o actualizar", required = true)
            @RequestBody List<Category> category) {
        try {
            List<Category> savedRecipes = categoryService.saveAll(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Eliminar categoría por ID", description = "Elimina una categoría específica dado su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error al eliminar la categoría")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @Parameter(description = "ID de la categoría a eliminar", required = true)
            @PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Categoría eliminada con éxito");
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría no encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la categoría");
        }
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
