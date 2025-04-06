package org.example.calories.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserCaloriesNorm {
    private long id;
    private int calorieDayNorm;
}
