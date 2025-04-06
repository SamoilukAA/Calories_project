package org.example.calories.service;

import org.example.calories.controller.CaloriesChecker;
import org.example.calories.controller.DailyReport;
import org.example.calories.controller.History;
import org.example.calories.controller.UserCaloriesNorm;
import org.example.calories.error.*;
import org.example.calories.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CaloriesServiceImpl implements CaloriesService {
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final MealRepository mealRepository;

    public CaloriesServiceImpl(UserRepository userRepository, DishRepository dishRepository,
                               MealRepository mealRepository) {
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
        this.mealRepository = mealRepository;
    }

    @Override
    public UserCaloriesNorm createUser(User userRequest) {
        double norm = 88.36 + (13.4 * userRequest.getUserWeight())
                      + (4.8 * userRequest.getUserHeight())
                      - (5.7 * userRequest.getUserAge());
        double percent = norm * 0.15;
        switch (userRequest.getPurpose()) {
            case "Похудение":
                norm -= percent;
                break;
            case "Набор массы":
                norm += percent;
                break;
        }
        int caloriesDayNorm = (int)Math.round(norm);
        userRequest.setCaloriesDayNorm(caloriesDayNorm);
        User user = userRepository.save(userRequest);

        return new UserCaloriesNorm(user.getId(), user.getCaloriesDayNorm());
    }

    @Override
    public ResponseEntity<User> getUser(long userId) {
        return userRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public String createDish(Dish dishRequest) {
        Dish dish = dishRepository.save(dishRequest);
        return "Dish created: " + dish.getId();
    }

    @Override
    public ResponseEntity<Dish> getDish(long dishId) {
        return dishRepository.findById(dishId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new DishNotFoundException(dishId));
    }

    @Override
    public String createMeal(Meal mealRequest) {
        if (mealRequest.getMealDate() == null) {
            mealRequest.setMealDate(LocalDate.now());
        }
        Meal meal = mealRepository.save(mealRequest);
        return "Meal created: " + meal.getId();
    }

    @Override
    public ResponseEntity<Meal> getMeal(long mealId) {
        return mealRepository.findById(mealId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new MealNotFoundException(mealId));
    }

    @Override
    public DailyReport getReport(long userId, LocalDate mealDate) {
        List<Meal> mealList = mealRepository.findByUserIdAndMealDate(userId, mealDate);
        if (mealList.isEmpty()) {
            throw new DateOrUserNotFoundException(userId, mealDate);
        }
        int calories = 0;
        LinkedHashMap<String, ArrayList<String>> mealsPerDay = new LinkedHashMap<>();
        for (Meal meal : mealList) {
            List<Dish> mealDishes = meal.getDishes();
            calories += mealDishes.stream()
                                  .mapToInt(Dish::getCalories)
                                  .sum();
            mealsPerDay.put(meal.getMealName(),
                    mealDishes.stream()
                              .map(Dish::getDishName)
                              .collect(Collectors.toCollection(ArrayList::new)));
        }
        return new DailyReport(calories, mealsPerDay);
    }

    @Override
    public CaloriesChecker checkIntake(long userId, LocalDate mealDate) {
        ResponseEntity<User> user = getUser(userId);
        int caloriesDayNorm = Objects.requireNonNull(user.getBody()).getCaloriesDayNorm();
        List<Meal> mealList = mealRepository.findByUserIdAndMealDate(userId, mealDate);
        if (mealList.isEmpty()) {
            throw new DateNotFoundException(mealDate);
        }
        int caloriesIntake = 0;
        LinkedHashMap<String, ArrayList<String>> mealsPerDay = new LinkedHashMap<>();
        for (Meal meal : mealList) {
            List<Dish> mealDishes = meal.getDishes();
            caloriesIntake += mealDishes.stream()
                                        .mapToInt(Dish::getCalories)
                                        .sum();
        }
        if (caloriesIntake <= caloriesDayNorm) {
            return new CaloriesChecker(caloriesDayNorm, caloriesIntake, "Whithin the norm");
        } else {
            return new CaloriesChecker(caloriesDayNorm, caloriesIntake, "Out of the norm");
        }
    }

    @Override
    public History getHistory(long userId) {
        List<Meal> mealList = mealRepository.findByUserId(userId);
        if (mealList.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Map<LocalDate, List<Meal>> mealsPerDate = mealList.stream()
                .collect(Collectors.groupingBy(Meal::getMealDate));
        LinkedHashMap<LocalDate, DailyReport> history = new LinkedHashMap<>();
        for (Map.Entry<LocalDate, List<Meal>> entry : mealsPerDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<Meal> meals = entry.getValue();

            LinkedHashMap<String, ArrayList<String>> mealsPerDay = new LinkedHashMap<>();

            int calories = 0;
            for (Meal meal : meals) {
                List<Dish> mealDishes = meal.getDishes();
                calories += mealDishes.stream()
                        .mapToInt(Dish::getCalories)
                        .sum();
                mealsPerDay.put(meal.getMealName(),
                        mealDishes.stream()
                                .map(Dish::getDishName)
                                .collect(Collectors.toCollection(ArrayList::new)));
            }
            DailyReport dailyReport = new DailyReport(calories, mealsPerDay);
            history.put(date, dailyReport);
        }
        return new History(history);
    }
}
