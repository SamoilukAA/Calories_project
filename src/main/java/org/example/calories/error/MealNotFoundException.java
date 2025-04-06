package org.example.calories.error;

public class MealNotFoundException extends RuntimeException {
    private final long mealId;

    public MealNotFoundException(long mealId) {
    this.mealId = mealId;
  }

    @Override
    public String getMessage() {
    return "Meal " + mealId + " not found";
  }
}
