package org.example.calories.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "meals_info")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate mealDate;

    @NotNull
    @Pattern(regexp = "Завтрак|Обед|Ужин|Перекус")
    private String mealName;

    @ManyToOne
    @CollectionTable(name = "users_info", joinColumns = @JoinColumn(name = "id"))
    private User user;

    @ManyToMany
    @JoinTable(name = "dishes_in_meal")
    private List<Dish> dishes;
}
