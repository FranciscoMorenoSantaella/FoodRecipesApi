package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import com.santaellamorenofrancisco.FoodRecipes.model.Ingredients;
import com.santaellamorenofrancisco.FoodRecipes.model.Category;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeCategoryService;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeService;
import com.santaellamorenofrancisco.FoodRecipes.repository.IngredientsRepository; // Asegúrate de tener este import
import com.santaellamorenofrancisco.FoodRecipes.repository.CategoryRepository; // Asegúrate de tener este import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientsRepository ingredientsRepository; // Repositorio para ingredientes

    @Autowired
    private CategoryRepository categoryRepository; // Repositorio para categorías
    
    @Autowired
    private RecipeCategoryService recipeCategoryService; // Repositorio para categorías

    // Obtener todas las recetas
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    // Obtener receta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipe);
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentra
        }
    }

    // Crear o actualizar receta
    @PostMapping()
    public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipe) {
        try {
            System.out.println(recipe);

            // Verificar ingredientes existentes
            Set<RecipeIngredients> recipeIngredientsSet = new HashSet<>();
            for (RecipeIngredients recipeIngredient : recipe.getRecipeIngredients()) {
                Optional<Ingredients> existingIngredient = ingredientsRepository.findById(recipeIngredient.getIngredients().getId());
                if (existingIngredient.isPresent()) {
                    recipeIngredientsSet.add(new RecipeIngredients(recipe, existingIngredient.get(), recipeIngredient.getQuantity()));
                }
            }
            recipe.setRecipeIngredients(recipeIngredientsSet);

            // Verificar categorías existentes
            Set<RecipeCategory> recipeCategorySet = new HashSet<>();
            for (RecipeCategory recipeCategory : recipe.getRecipeCategories()) {
                Optional<Category> existingCategory = categoryRepository.findById(recipeCategory.getCategory().getId());
                if (existingCategory.isPresent()) {
                    recipeCategorySet.add(new RecipeCategory(recipe, existingCategory.get()));
                }
            }
            recipe.setRecipeCategories(recipeCategorySet);

            // Llama al servicio con la receta
            Recipe savedRecipe = recipeService.saveOrUpdateRecipe(recipe); 
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe); // 201 si se crea con éxito
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 si hay un error al guardar
        }
    }

    // Eliminar receta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipe(id);
            return ResponseEntity.ok("Receta eliminada con éxito");
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receta no encontrada"); // 404 si no se encuentra
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la receta"); // 500 si hay error al eliminar
        }
    }
    
    @PostMapping("/image")
    public ResponseEntity<Recipe> createOrUpdateRecipe(@RequestPart("recipe") Recipe recipe, @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            Recipe savedRecipe = recipeService.saveOrUpdateRecipeImage(recipe, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Buscar recetas por dificultad
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<Recipe>> getRecipesByDifficulty(@PathVariable String difficulty) {
        try {
            List<Recipe> recipes = recipeService.getRecipesByDifficulty(difficulty);
            return ResponseEntity.ok(recipes);
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentran recetas con la dificultad especificada
        }
    }

    // Buscar recetas cuyo tiempo de preparación sea menor o igual a un valor
    @GetMapping("/time/{maxTime}")
    public ResponseEntity<List<Recipe>> getRecipesByMaxTime(@PathVariable int maxTime) {
        try {
            List<Recipe> recipes = recipeService.getRecipesByMaxTime(maxTime);
            return ResponseEntity.ok(recipes);
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentran recetas dentro del tiempo especificado
        }
    }
    
    @PostMapping("/list")
    public ResponseEntity<List<Recipe>> saveRecipes(@RequestBody List<Recipe> recipes) {
        try {
            List<Recipe> savedRecipes = recipeService.saveAll(recipes);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Buscar recetas por dificultad
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Recipe>> getRecipesByCategoryName(@PathVariable String categoryName) {
        try {
            List<Recipe> recipes = recipeCategoryService.getRecipesByCategoryName(categoryName);
            return ResponseEntity.ok(recipes);
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si no se encuentran recetas con la dificultad especificada
        }
    }

    // Manejo de excepciones global
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<String> handleRecipeNotFoundException(RecipeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
