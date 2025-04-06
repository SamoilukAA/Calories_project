package org.example.calories.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DailyReport {
    private int calories;
    private LinkedHashMap<String, ArrayList<String>> mealsPerDay;
}
