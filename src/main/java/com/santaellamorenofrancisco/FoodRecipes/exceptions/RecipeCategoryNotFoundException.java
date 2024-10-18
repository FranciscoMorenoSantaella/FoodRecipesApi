package com.santaellamorenofrancisco.FoodRecipes.exceptions;

public class RecipeCategoryNotFoundException extends RuntimeException {
    public RecipeCategoryNotFoundException(String message) {
        super(message);
    }
}