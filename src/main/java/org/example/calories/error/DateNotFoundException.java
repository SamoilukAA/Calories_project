package org.example.calories.error;

import java.time.LocalDate;

public class DateNotFoundException extends RuntimeException {
    private final LocalDate date;

    public DateNotFoundException(LocalDate date) {
        this.date = date;
    }

    @Override
    public String getMessage() {
        return "Date = " + date + " not found";
    }
}
