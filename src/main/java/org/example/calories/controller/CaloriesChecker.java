package org.example.calories.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CaloriesChecker {
    private int caloriesDayNorm;
    private int caloriesIntake;
    private String comment;
}
