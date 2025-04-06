package org.example.calories.error;

public class DishNotFoundException extends RuntimeException {
    private final long dishId;

    public DishNotFoundException(long dishId) {
        this.dishId = dishId;
    }

    @Override
    public String getMessage() {
        return "Dish with id = " + dishId + " not found";
    }
}
