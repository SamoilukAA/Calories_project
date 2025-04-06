package org.example.calories.service;

import org.example.calories.controller.CaloriesChecker;
import org.example.calories.controller.DailyReport;
import org.example.calories.controller.History;
import org.example.calories.controller.UserCaloriesNorm;
import org.example.calories.repository.Dish;
import org.example.calories.repository.Meal;
import org.example.calories.repository.User;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface CaloriesService {
    UserCaloriesNorm createUser(User userRequest);
    ResponseEntity<User> getUser(long userId);
    String createDish(Dish dishRequest);
    ResponseEntity<Dish> getDish(long dishId);
    String createMeal(Meal mealRequest);
    ResponseEntity<Meal> getMeal(long mealId);
    DailyReport getReport(long userId, LocalDate mealDate);
    CaloriesChecker checkIntake(long userId, LocalDate mealDate);
    History getHistory(long userId);
}
