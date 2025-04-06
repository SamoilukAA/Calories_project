package org.example.calories;
import jakarta.validation.ConstraintViolationException;
import org.example.calories.controller.CaloriesChecker;
import org.example.calories.controller.DailyReport;
import org.example.calories.controller.History;
import org.example.calories.controller.UserCaloriesNorm;
import org.example.calories.error.*;
import org.example.calories.repository.*;
import org.example.calories.service.CaloriesServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CaloriesApplicationTests {

    @Autowired
    private CaloriesServiceImpl caloriesService;

    @Test
    public void testCanCreateUser() {
        User user = new User();
        user.setUserName("testUser");
        user.setEmail("test@test.com");
        user.setUserAge(30);
        user.setUserWeight(76.3);
        user.setUserHeight(183.0);
        user.setPurpose("Поддержание");

        UserCaloriesNorm userCaloriesNorm = caloriesService.createUser(user);

        assertThat(userCaloriesNorm).isEqualTo(new UserCaloriesNorm(userCaloriesNorm.getId(), 1818));
    }

    @Test
    public void testCantCreateUserWithSameEmail() {
        User user = new User();
        user.setUserName("nastya");
        user.setEmail("nastya@gmail.com");
        user.setUserAge(30);
        user.setUserWeight(76.3);
        user.setUserHeight(183.0);
        user.setPurpose("Поддержание");

        assertThrows(DataIntegrityViolationException.class, () -> caloriesService.createUser(user));
    }

    @Test
    public void testCanGetUser() {
        long userId = 1L;
        User user = new User(1L, "nastya",
                             "nastya@gmail.com", 23, 50.9,
                             163.0, "Похудение", 1208);

        ResponseEntity<User> actual = caloriesService.getUser(userId);

        assertThat(actual.getBody()).isEqualTo(user);
    }

    @Test
    public void testCantGetUserWithNonExistingId() {
        long userId = 110L;

        assertThrows(UserNotFoundException.class, () -> caloriesService.getUser(userId));
    }

    @Test
    public void testCanCreateDish() {
        Dish dish = new Dish();
        dish.setDishName("testDish");
        dish.setCalories(300);
        dish.setPfc("10/8/20");

        String return_str = caloriesService.createDish(dish);

        assertTrue(return_str.contains("Dish created: "));
    }

    @Test
    public void testCantCreateDishWithSameName() {
        Dish dish = new Dish();
        dish.setDishName("Овсянка с ягодами");
        dish.setCalories(300);
        dish.setPfc("10/5/55");

        assertThrows(DataIntegrityViolationException.class, () -> caloriesService.createDish(dish));
    }

    @Test
    public void testCanGetDish() {
        long dishId = 1L;
        Dish dish = new Dish(1L, "Овсянка с ягодами", 300, "10/5/55");

        ResponseEntity<Dish> actual = caloriesService.getDish(dishId);

        assertThat(actual.getBody()).isEqualTo(dish);
    }

    @Test
    public void testCantGetDishWithNonExistingId() {
        long dishId = 110L;

        assertThrows(DishNotFoundException.class, () -> caloriesService.getDish(dishId));
    }

    @Test
    public void testCanCreateMeal() {
        Meal meal = new Meal();
        meal.setMealName("Завтрак");
        User user = new User();
        user.setId(1L);
        meal.setUser(user);
        Dish dish = new Dish();
        dish.setId(1L);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish);
        meal.setDishes(dishes);

        String return_str = caloriesService.createMeal(meal);

        assertTrue(return_str.contains("Meal created: "));
    }

    @Test
    public void testCantCreateMealWithWrongName() {
        Meal meal = new Meal();
        meal.setMealName("Второй завтрак");
        User user = new User();
        user.setId(1L);
        meal.setUser(user);
        Dish dish = new Dish();
        dish.setId(1L);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish);
        meal.setDishes(dishes);

        assertThrows(ConstraintViolationException.class, () -> caloriesService.createMeal(meal));
    }

    @Test
    public void testCanGetMeal() {
        long mealId = 1L;
        User user = new User(1L, "nastya",
                             "nastya@gmail.com", 23, 50.9,
                             163.0, "Похудение", 1208);
        Dish dish = new Dish(1L, "Овсянка с ягодами", 300, "10/5/55");
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish);
        Meal meal = new Meal(1L, LocalDate.parse("2025-04-05"), "Завтрак", user, dishes);

        ResponseEntity<Meal> actual = caloriesService.getMeal(mealId);

        assertThat(actual.getBody()).usingRecursiveComparison().isEqualTo(meal);
    }

    @Test
    public void testCantGetMealWithNonExistingId() {
        long mealId = 110L;

        assertThrows(MealNotFoundException.class, () -> caloriesService.getMeal(mealId));
    }

    @Test
    public void testCanGetReport() {
        long userId = 1L;
        LocalDate localDate = LocalDate.parse("2025-04-06");
        LinkedHashMap<String, ArrayList<String>> mealsPerDay = new LinkedHashMap<>();
        ArrayList<String> dishes_names = new ArrayList<>();
        dishes_names.add("Яичница с овощами");
        dishes_names.add("Овощные палочки с хумусом");
        mealsPerDay.put("Завтрак", dishes_names);
        DailyReport dailyReport = new DailyReport(430, mealsPerDay);

        DailyReport actual = caloriesService.getReport(userId, localDate);

        assertThat(actual).isEqualTo(dailyReport);
    }

    @Test
    public void testCantGetReportWithWrongUserIdOrWrongDate() {
        long userId = 110L;
        LocalDate localDate = LocalDate.parse("2025-04-06");

        assertThrows(DateOrUserNotFoundException.class, () -> caloriesService.getReport(userId, localDate));

        long newUserId = 1L;
        LocalDate newLocalDate = LocalDate.parse("2025-04-20");

        assertThrows(DateOrUserNotFoundException.class, () -> caloriesService.getReport(newUserId, newLocalDate));
    }

    @Test
    public void testCanCheckIntake() {
        long userId = 1L;
        LocalDate localDate = LocalDate.parse("2025-04-06");
        CaloriesChecker caloriesChecker = new CaloriesChecker(1208,
                                                              430, "Whithin the norm");

        CaloriesChecker actual = caloriesService.checkIntake(userId, localDate);

        assertThat(actual).isEqualTo(caloriesChecker);
    }

    @Test
    public void testCantCheckIntakeWithWrongUserId() {
        long userId = 110L;
        LocalDate localDate = LocalDate.parse("2025-04-06");

        assertThrows(UserNotFoundException.class, () -> caloriesService.checkIntake(userId, localDate));
    }

    @Test
    public void testCantCheckIntakeWithWrongDate() {
        long userId = 1L;
        LocalDate localDate = LocalDate.parse("2025-04-20");

        assertThrows(DateNotFoundException.class, () -> caloriesService.checkIntake(userId, localDate));
    }

    @Test
    public void testCanGetHistory() {
        long userId = 2L;
        LinkedHashMap<LocalDate, DailyReport> dayHistory = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<String>> mealsPerDay = new LinkedHashMap<>();
        mealsPerDay.put("Завтрак", new ArrayList<>(List.of("Яичница с овощами", "Авокадо на цельнозерновом тосте")));
        mealsPerDay.put("Обед", new ArrayList<>(List.of("Томатный суп с фасолью")));
        mealsPerDay.put("Ужин", new ArrayList<>(List.of("Тушеная говядина с картофелем и морковью")));
        mealsPerDay.put("Перекус", new ArrayList<>(List.of("Протеиновый батончик с орехами", "Авокадо на цельнозерновом тосте")));
        DailyReport dailyReport = new DailyReport(1850, mealsPerDay);
        dayHistory.put(LocalDate.parse("2025-04-05"), dailyReport);
        History history = new History(dayHistory);

        History actual = caloriesService.getHistory(userId);

        assertThat(actual).isEqualTo(history);
    }

    @Test
    public void testCantGetHistoryWithWrongUserId() {
        long userId = 110L;

        assertThrows(UserNotFoundException.class, () -> caloriesService.getHistory(userId));
    }
}
