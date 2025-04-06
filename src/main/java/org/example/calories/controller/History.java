package org.example.calories.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class History {
    private LinkedHashMap<LocalDate, DailyReport> history;
}
