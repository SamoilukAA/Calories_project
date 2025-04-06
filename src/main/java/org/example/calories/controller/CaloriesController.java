package org.example.calories.controller;

import jakarta.validation.Valid;
import org.example.calories.repository.Dish;
import org.example.calories.repository.Meal;
import org.example.calories.repository.User;
import org.example.calories.service.CaloriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/calories_checker")
public class CaloriesController {
    private final CaloriesService caloriesService;

    public CaloriesController(final CaloriesService caloriesService) {
        this.caloriesService = caloriesService;
    }

    @PostMapping(value = "/new_user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCaloriesNorm createUser(@Valid @RequestBody User userRequest) {
        return caloriesService.createUser(userRequest);
    }

    @GetMapping(value = "/users/{userId:\\d+}")
    public ResponseEntity<User> getUser(@PathVariable long userId) {
        return caloriesService.getUser(userId);
    }

    @PostMapping(value = "/new_dish")
    @ResponseStatus(HttpStatus.CREATED)
    public String createDish(@Valid @RequestBody Dish dishRequest) {
        return caloriesService.createDish(dishRequest);
    }

    @GetMapping(value = "/dishes/{dishId:\\d+}")
    public ResponseEntity<Dish> getDish(@PathVariable long dishId) {
        return caloriesService.getDish(dishId);
    }

    @PostMapping(value = "/new_meal")
    @ResponseStatus(HttpStatus.CREATED)
    public String createMeal(@Valid @RequestBody Meal mealRequest) {
        return caloriesService.createMeal(mealRequest);
    }

    @GetMapping(value = "/meals/{mealId:\\d+}")
    public ResponseEntity<Meal> getMeal(@PathVariable long mealId) {
        return caloriesService.getMeal(mealId);
    }

    @GetMapping(value = "/report/{userId:\\d+}/{mealDate:\\d{4}-\\d{2}-\\d{2}}")
    public DailyReport getReport(@PathVariable long userId, @PathVariable LocalDate mealDate) {
        return caloriesService.getReport(userId, mealDate);
    }

    @GetMapping(value = "/check/{userId:\\d+}/{mealDate:\\d{4}-\\d{2}-\\d{2}}")
    public CaloriesChecker checkIntake(@PathVariable long userId, @PathVariable LocalDate mealDate) {
        return caloriesService.checkIntake(userId, mealDate);
    }

    @GetMapping(value = "/history/{userId:\\d+}")
    public History getHistory(@PathVariable long userId) {
        return caloriesService.getHistory(userId);
    }
}
