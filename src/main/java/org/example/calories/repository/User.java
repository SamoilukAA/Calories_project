package org.example.calories.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "users_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userName;

    @Email
    @NotNull
    private String email;

    @Min(10)
    @Max(99)
    private int userAge;

    @DecimalMin("30")
    @DecimalMax("150")
    private double userWeight;

    @DecimalMin("120")
    @DecimalMax("220")
    private double userHeight;

    @Pattern(regexp = "Похудение|Поддержание|Набор массы")
    @NotNull
    private String purpose;

    private int caloriesDayNorm;
}
