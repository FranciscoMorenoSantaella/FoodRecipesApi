package com.santaellamorenofrancisco.FoodRecipes.services;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.IngredientsNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Ingredients;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.repository.IngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientsService {

    @Autowired
    private IngredientsRepository ingredientsRepository;

    // Obtener todos los ingredientes
    public List<Ingredients> getAllIngredients() {
        return ingredientsRepository.findAll();
    }

    // Obtener ingrediente por ID, arroja excepción si no se encuentra
    public Ingredients getIngredientById(Long id) {
        return ingredientsRepository.findById(id)
                .orElseThrow(() -> new IngredientsNotFoundException("Ingrediente con ID " + id + " no encontrado"));
    }

    // Obtener ingrediente por nombre
    public Ingredients getIngredientByName(String name) {
        return ingredientsRepository.findByName(name)
                .orElseThrow(() -> new IngredientsNotFoundException("Ingrediente con nombre '" + name + "' no encontrado"));
    }

    // Guardar o actualizar ingrediente
    public Ingredients saveOrUpdateIngredient(Ingredients ingredient) {
        try {
            return ingredientsRepository.save(ingredient);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el ingrediente: " + e.getMessage());
        }
    }
    
    // Método para guardar una lista de recetas
    public List<Ingredients> saveAll(List<Ingredients> ingredients) {
        return ingredientsRepository.saveAll(ingredients); // Utiliza saveAll de JpaRepository
    }


    // Eliminar ingrediente por ID, arroja excepción si no se encuentra
    public void deleteIngredient(Long id) {
        Ingredients ingredient = getIngredientById(id);  // Si no existe, lanza IngredientNotFoundException
        try {
            ingredientsRepository.delete(ingredient);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el ingrediente con ID " + id + ": " + e.getMessage());
        }
    }
}
