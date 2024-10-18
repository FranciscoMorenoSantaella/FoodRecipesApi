package com.santaellamorenofrancisco.FoodRecipes.services;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.CategoryNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Category;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Obtener todas las categorías
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Obtener categoría por ID, arroja excepción si no se encuentra
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría con ID " + categoryId + " no encontrada"));
    }

    // Obtener categoría por nombre
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría con nombre '" + name + "' no encontrada"));
    }

    // Guardar o actualizar categoría
    public Category saveOrUpdateCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la categoría: " + e.getMessage());
        }
    }
    
    // Método para guardar una lista de categories
    public List<Category> saveAll(List<Category> category) {
        return categoryRepository.saveAll(category); // Utiliza saveAll de JpaRepository
    }

    // Eliminar categoría por ID, arroja excepción si no se encuentra
    public void deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);  // Si no existe, lanza CategoryNotFoundException
        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la categoría con ID " + categoryId + ": " + e.getMessage());
        }
    }
}
