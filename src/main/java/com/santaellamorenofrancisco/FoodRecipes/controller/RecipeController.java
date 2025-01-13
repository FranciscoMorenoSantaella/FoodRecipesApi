package com.santaellamorenofrancisco.FoodRecipes.controller;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import com.santaellamorenofrancisco.FoodRecipes.model.Ingredients;
import com.santaellamorenofrancisco.FoodRecipes.model.Category;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeCategoryService;
import com.santaellamorenofrancisco.FoodRecipes.services.RecipeService;
import com.santaellamorenofrancisco.FoodRecipes.repository.IngredientsRepository;
import com.santaellamorenofrancisco.FoodRecipes.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springdoc.core.converters.models.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "https://foodrecipesapi-production-07df.up.railway.app")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private RecipeCategoryService recipeCategoryService;

	@Operation(summary = "Obtiene todas las recetas")
	@ApiResponse(responseCode = "200", description = "Lista de recetas obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
	@GetMapping
	public ResponseEntity<List<Recipe>> getAllRecipes() {
		List<Recipe> recipes = recipeService.getAllRecipes();
		return ResponseEntity.ok(recipes);
	}

	@Operation(summary = "Obtiene una receta por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Receta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
			@ApiResponse(responseCode = "404", description = "Receta no encontrada", content = @Content(mediaType = "application/json")) })
	@GetMapping("/{id}")
	public ResponseEntity<Recipe> getRecipeById(@Parameter(description = "ID de la receta") @PathVariable Long id) {
		try {
			Recipe recipe = recipeService.getRecipeById(id);
			return ResponseEntity.ok(recipe);
		} catch (RecipeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@Operation(summary = "Crea o actualiza una receta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Receta creada o actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
			@ApiResponse(responseCode = "500", description = "Error al guardar la receta", content = @Content(mediaType = "application/json")) })
	@PostMapping()
	public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipe) {
		try {
			Set<RecipeIngredients> recipeIngredientsSet = new HashSet<>();
			for (RecipeIngredients recipeIngredient : recipe.getRecipeIngredients()) {
				Optional<Ingredients> existingIngredient = ingredientsRepository
						.findById(recipeIngredient.getIngredients().getId());
				if (existingIngredient.isPresent()) {
					recipeIngredientsSet.add(new RecipeIngredients(recipe, existingIngredient.get(),
							recipeIngredient.getQuantity(), recipeIngredient.getType_quantity()));
				}
			}
			recipe.setRecipeIngredients(recipeIngredientsSet);

			Set<RecipeCategory> recipeCategorySet = new HashSet<>();
			for (RecipeCategory recipeCategory : recipe.getRecipeCategories()) {
				Optional<Category> existingCategory = categoryRepository.findById(recipeCategory.getCategory().getId());
				if (existingCategory.isPresent()) {
					recipeCategorySet.add(new RecipeCategory(recipe, existingCategory.get()));
				}
			}
			recipe.setRecipeCategories(recipeCategorySet);

			Recipe savedRecipe = recipeService.saveOrUpdateRecipe(recipe);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Elimina una receta por su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Receta eliminada exitosamente", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Receta no encontrada", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Error al eliminar la receta", content = @Content(mediaType = "application/json")) })
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRecipe(
			@Parameter(description = "ID de la receta a eliminar") @PathVariable Long id) {
		try {
			recipeService.deleteRecipe(id);
			return ResponseEntity.ok("Receta eliminada con éxito");
		} catch (RecipeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receta no encontrada");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la receta");
		}
	}

	@Operation(summary = "Crear o actualizar una receta con imagen")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Receta creada o actualizada con imagen", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
			@ApiResponse(responseCode = "400", description = "Error en la solicitud", content = @Content(mediaType = "application/json")) })
	@PostMapping("/image")
	public ResponseEntity<Recipe> createOrUpdateRecipe(
			@Parameter(description = "Datos de la receta") @RequestPart("recipe") Recipe recipe,
			@Parameter(description = "Archivo de imagen") @RequestPart("imageFile") MultipartFile imageFile) {
		try {
			System.out.println(recipe.getDifficulty());
			Recipe savedRecipe = recipeService.saveOrUpdateRecipeImage(recipe, imageFile);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@Operation(summary = "Buscar recetas por dificultad")
	@ApiResponse(responseCode = "200", description = "Recetas encontradas por dificultad", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
	@GetMapping("/difficulty/{difficulty}")
	public ResponseEntity<List<Recipe>> getRecipesByDifficulty(
			@Parameter(description = "Dificultad de las recetas") @PathVariable String difficulty) {
		try {
			List<Recipe> recipes = recipeService.getRecipesByDifficulty(difficulty);
			return ResponseEntity.ok(recipes);
		} catch (RecipeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@Operation(summary = "Buscar recetas por tiempo máximo de preparación")
	@ApiResponse(responseCode = "200", description = "Recetas encontradas por tiempo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
	@GetMapping("/time/{maxTime}")
	public ResponseEntity<List<Recipe>> getRecipesByMaxTime(
			@Parameter(description = "Tiempo máximo de preparación") @PathVariable int maxTime) {
		try {
			List<Recipe> recipes = recipeService.getRecipesByMaxTime(maxTime);
			return ResponseEntity.ok(recipes);
		} catch (RecipeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@Operation(summary = "Crear o actualizar múltiples recetas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Recetas creadas o actualizadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
			@ApiResponse(responseCode = "500", description = "Error al crear o actualizar recetas", content = @Content(mediaType = "application/json")) })
	@PostMapping("/list")
	public ResponseEntity<List<Recipe>> saveRecipes(@RequestBody List<Recipe> recipes) {
		try {
			List<Recipe> savedRecipes = recipeService.saveAll(recipes);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Buscar recetas por nombre de categoría")
	@ApiResponse(responseCode = "200", description = "Recetas encontradas por categoría", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
	@GetMapping("/category/{categoryName}")
	public ResponseEntity<List<Recipe>> getRecipesByCategoryName(
			@Parameter(description = "Nombre de la categoría de las recetas") @PathVariable String categoryName) {
		try {
			List<Recipe> recipes = recipeCategoryService.getRecipesByCategoryName(categoryName);
			return ResponseEntity.ok(recipes);
		} catch (RecipeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@ExceptionHandler(RecipeNotFoundException.class)
	public ResponseEntity<String> handleRecipeNotFoundException(RecipeNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/pageable/{pagenumber}/{pagesize}", method = RequestMethod.GET)
	public ResponseEntity<Page<Recipe>> getRecipePageable(@PathVariable int pagenumber, @PathVariable int pagesize) {
		if (pagenumber >= 0 && pagesize >= 0) {
			try {
				Page<Recipe> pageRecipes = recipeService.getRecipeByPage(pagenumber, pagesize);
				return ResponseEntity.ok(pageRecipes);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/pageable/name/{recipename}/{pagenumber}/{pagesize}", method = RequestMethod.GET)
	public ResponseEntity<Page<Recipe>> getRecipeByNamePageable(@PathVariable String recipename,
			@PathVariable int pagenumber, @PathVariable int pagesize) {
		if (pagenumber >= 0 && pagesize >= 0) {
			try {
				Page<Recipe> pageRecipes = recipeService.getRecipeByNamePageable(recipename, pagenumber, pagesize);
				return ResponseEntity.ok(pageRecipes);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/pageable/{categoryname}/{pagenumber}/{pagesize}", method = RequestMethod.GET)
	public ResponseEntity<Page<Recipe>> getRecipePageable(@PathVariable String categoryname,
			@PathVariable int pagenumber, @PathVariable int pagesize) {
		if (pagenumber >= 0 && pagesize >= 0) {
			try {
				Page<Recipe> pageRecipes = recipeService.getRecipesByCategoryNamePageable(categoryname, pagenumber,
						pagesize);
				return ResponseEntity.ok(pageRecipes);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
