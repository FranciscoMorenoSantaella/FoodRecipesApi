package com.santaellamorenofrancisco.FoodRecipes.repository;

import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	// Consulta personalizada para buscar recetas por dificultad
	List<Recipe> findByDifficulty(String difficulty);

	// Consulta personalizada para buscar recetas con un tiempo de preparación menor
	// o igual a un valor dado
	List<Recipe> findByTimeLessThanEqual(int time);

	@Query(value = "SELECT r.* FROM recipe_category rc, recipe r, category c WHERE c.name = ?1 AND r.id = rc.recipe_id AND c.id = rc.category_id", countQuery = "SELECT COUNT(*) FROM recipe_category rc, recipe r, category c WHERE c.name = ?1 AND r.id = rc.recipe_id AND c.id = rc.category_id", nativeQuery = true)
	Page<Recipe> findRecipesByCategoryNamePageable(String categoryName, Pageable pageable);

	@Query(
		    value = """
		        SELECT * 
		        FROM recipe r 
		        WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', ?1, '%'))
		    """,
		    countQuery = """
		        SELECT COUNT(*) 
		        FROM recipe r 
		        WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', ?1, '%'))
		    """,
		    nativeQuery = true
		)
		Page<Recipe> getRecipeByNamePageable(String recipename, Pageable pageable);

}
