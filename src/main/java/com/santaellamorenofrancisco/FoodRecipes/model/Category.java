package com.santaellamorenofrancisco.FoodRecipes.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon", nullable = false)
    private String icon;
    
    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    // Relación con RecipeCategory
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecipeCategory> recipeCategories = new HashSet<>();

    // Constructor vacío
    public Category() {
    }

    // Constructor con parámetros
    public Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Set<RecipeCategory> getRecipeCategories() {
        return recipeCategories;
    }

    public void setRecipeCategories(Set<RecipeCategory> recipeCategories) {
        this.recipeCategories = recipeCategories;
    }

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", icon=" + icon + ", recipeCategories=" + recipeCategories
				+ "]";
	}
    
}
