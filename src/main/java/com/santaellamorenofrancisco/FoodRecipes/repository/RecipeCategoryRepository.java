package com.santaellamorenofrancisco.FoodRecipes.repository;

import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {

    // Consultas personalizadas
    List<RecipeCategory> findByRecipeId(Long recipeId);

    List<RecipeCategory> findByCategoryId(Long categoryId);
    
    // Consulta para obtener todas las relaciones de RecipeCategory por nombre de la categoría
    @Query("SELECT rc.recipe FROM RecipeCategory rc WHERE rc.category.name = :categoryName")
    List<Recipe> findRecipesByCategoryName(@Param("categoryName") String categoryName);

}
