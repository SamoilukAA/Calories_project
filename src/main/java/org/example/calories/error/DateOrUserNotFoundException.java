package org.example.calories.error;

import java.time.LocalDate;

public class DateOrUserNotFoundException extends RuntimeException {
    private final long userId;
    private final LocalDate date;

    public DateOrUserNotFoundException(long userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }

    @Override
    public String getMessage() {
        return "User = " + userId + " or date = " + date + " not found";
  }
}
