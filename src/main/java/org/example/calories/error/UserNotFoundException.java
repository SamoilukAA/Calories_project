package org.example.calories.error;

public class UserNotFoundException extends RuntimeException {
    private final long userId;

    public UserNotFoundException(long userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return "User " + userId + " not found";
    }
}
