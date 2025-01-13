package com.santaellamorenofrancisco.FoodRecipes.services;

import com.santaellamorenofrancisco.FoodRecipes.exceptions.CategoryNotFoundException;
import com.santaellamorenofrancisco.FoodRecipes.model.Category;
import com.santaellamorenofrancisco.FoodRecipes.model.Recipe;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeCategory;
import com.santaellamorenofrancisco.FoodRecipes.model.RecipeIngredients;
import com.santaellamorenofrancisco.FoodRecipes.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
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
    
    // Guarda la categoria y si ya existe la actualiza 
    public Category saveOrUpdateCategoryImage(Category category, MultipartFile imageFile) {
        try {
            // Si se pasa un archivo de imagen, lo subimos a Cloudinary
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadResult = cloudinaryService.uploadImage(imageFile);
                String imageUrl = (String) uploadResult;  // Obtener la URL de la imagen
                category.setImageUrl(imageUrl);  // Guardar la URL de la imagen en la categoria
            }

            // Guardar la categoria
            Category savedCategory = categoryRepository.save(category);

            return savedCategory;  // Retornar la categoria guardada
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la receta: " + e.getMessage());
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
    
    // Obtiene la categoria segun la pagina y el tamaño de la pagina
	public Page<Category> getCategoryByPage(int pagenumber, int pagesize) throws Exception {
		if (pagenumber >= 0 && pagesize >= 0) {
			try {
				Sort sort = Sort.by(Sort.Direction.ASC, "name");
				Pageable page = PageRequest.of(pagenumber, pagesize, sort);
				
				return categoryRepository.findAll(page); 
			} catch (Exception e) {
				throw new Exception("Error en la consulta", e);
			}
		} else {
			throw new Exception("El numero de pagina y/o el limite no puede ser menor que 0");
		}
	}
}
