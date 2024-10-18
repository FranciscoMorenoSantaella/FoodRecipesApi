package com.santaellamorenofrancisco.FoodRecipes.repository;

import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Consulta personalizada para buscar recetas por dificultad
    List<Recipe> findByDifficulty(String difficulty);

    // Consulta personalizada para buscar recetas con un tiempo de preparación menor o igual a un valor dado
    List<Recipe> findByTimeLessThanEqual(int time);
}
