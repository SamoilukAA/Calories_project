package org.example.calories.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndMealDate(long userId, LocalDate meal_date);
    List<Meal> findByUserId(long userId);
}
