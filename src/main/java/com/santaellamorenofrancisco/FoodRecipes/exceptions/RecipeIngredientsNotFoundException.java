package com.santaellamorenofrancisco.FoodRecipes.exceptions;

public class RecipeIngredientsNotFoundException extends RuntimeException {

    public RecipeIngredientsNotFoundException(String message) {
        super(message);
    }
}