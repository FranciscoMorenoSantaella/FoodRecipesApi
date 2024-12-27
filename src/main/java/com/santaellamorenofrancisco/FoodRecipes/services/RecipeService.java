package com.santaellamorenofrancisco.FoodRecipes.services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.santaellamorenofrancisco.FoodRecipes.exceptions.RecipeNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import com.santaellamorenofrancisco.FoodRecipes.repository.RecipeCategoryRepository;
import com.santaellamorenofrancisco.FoodRecipes.repository.RecipeIngredientsRepository;
import com.santaellamorenofrancisco.FoodRecipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CloudinaryService cloudinaryService; // Asegúrate de inyectar el CloudinaryService

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;
    
    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;
    // Obtener todas las recetas
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Obtener receta por ID, arroja excepción si no se encuentra
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Receta con ID " + id + " no encontrada"));
    }

    // Guardar una nueva receta o actualizar una existente
    public Recipe saveOrUpdateRecipe(Recipe recipe) {
        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la receta: " + e.getMessage());
        }
    }
    public Recipe saveOrUpdateRecipeImage(Recipe recipe, MultipartFile imageFile) {
        try {
            // Si se pasa un archivo de imagen, lo subimos a Cloudinary
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadResult = cloudinaryService.uploadImage(imageFile);
                String imageUrl = (String) uploadResult;  // Obtener la URL de la imagen
                recipe.setImageUrl(imageUrl);  // Guardar la URL de la imagen en la receta
            }

            // Guardar la receta
            Recipe savedRecipe = recipeRepository.save(recipe);

            // Guardar las relaciones con las categorías
            for (RecipeCategory recipeCategory : recipe.getRecipeCategories()) {
                recipeCategory.setRecipe(savedRecipe);  // Establecer la relación con la receta
            }
            recipeCategoryRepository.saveAll(recipe.getRecipeCategories());  // Persistir las categorías

            // Guardar las relaciones con los ingredientes
            for (RecipeIngredients recipeIngredient : recipe.getRecipeIngredients()) {
                recipeIngredient.setRecipe(savedRecipe);  // Establecer la relación con la receta
            }
            recipeIngredientsRepository.saveAll(recipe.getRecipeIngredients());  // Persistir los ingredientes

            return savedRecipe;  // Retornar la receta guardada
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la receta: " + e.getMessage());
        }
    }

    
    
    // Método para guardar una lista de recetas
    public List<Recipe> saveAll(List<Recipe> recipes) {
        return recipeRepository.saveAll(recipes); // Utiliza saveAll de JpaRepository
    }

    // Eliminar una receta por ID, arroja excepción si no se encuentra
    public void deleteRecipe(Long id) {
        Recipe recipe = getRecipeById(id);  // Si no existe, lanzará la excepción RecipeNotFoundException
        try {
            recipeRepository.delete(recipe);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la receta con ID " + id + ": " + e.getMessage());
        }
    }

    // Consulta personalizada: Buscar recetas por dificultad
    public List<Recipe> getRecipesByDifficulty(String difficulty) {
        List<Recipe> recipes = recipeRepository.findByDifficulty(difficulty);
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No se encontraron recetas con la dificultad: " + difficulty);
        }
        return recipes;
    }

    // Consulta personalizada: Buscar recetas cuyo tiempo de preparación sea menor o igual al tiempo dado
    public List<Recipe> getRecipesByMaxTime(int maxTime) {
        List<Recipe> recipes = recipeRepository.findByTimeLessThanEqual(maxTime);
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No se encontraron recetas con un tiempo menor o igual a: " + maxTime + " minutos");
        }
        return recipes;
    }
    
	public Page<Recipe> getRecipeByPage(int pagenumber, int pagesize) throws Exception {
		if (pagenumber >= 0 && pagesize >= 0) {
			try {
				Sort sort = Sort.by(Sort.Direction.ASC, "name");
				Pageable page = PageRequest.of(pagenumber, pagesize, sort);
				
				return recipeRepository.findAll(page); 
			} catch (Exception e) {
				throw new Exception("Error en la consulta", e);
			}
		} else {
			throw new Exception("El numero de pagina y/o el limite no puede ser menor que 0");
		}
	}
	
	public Page<Recipe> getRecipeByNamePageable(String recipename,int pagenumber, int pagesize) throws Exception {
		if (pagenumber >= 0 && pagesize >= 0) {
			try {
				Sort sort = Sort.by(Sort.Direction.ASC, "name");
				Pageable page = PageRequest.of(pagenumber, pagesize, sort);
				
				return recipeRepository.getRecipeByNamePageable(recipename, page);
			} catch (Exception e) {
				throw new Exception("Error en la consulta", e);
			}
		} else {
			throw new Exception("El numero de pagina y/o el limite no puede ser menor que 0");
		}
	}
	
	
    public Page<Recipe> getRecipesByCategoryNamePageable(String categoryName, int page, int pagesize) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "id");
    	Pageable pageable = PageRequest.of(page, pagesize,sort); // Crea un objeto Pageable con el número de página y tamaño.
        Page<Recipe> recipesPage = recipeRepository.findRecipesByCategoryNamePageable(categoryName, pageable); // Llama al método del repositorio.

        return recipesPage;
    }
	
    
}
