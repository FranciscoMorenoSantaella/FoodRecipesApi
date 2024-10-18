package com.santaellamorenofrancisco.FoodRecipes.exceptions;

public class IngredientsNotFoundException extends RuntimeException {

    public IngredientsNotFoundException(String message) {
        super(message);
    }
}
