package com.svalero.roadrunner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotBlank(message = "The field cannot be empty")
    @NotNull(message = "The field is required")
    private String name;
    @Column
    private String surname;
    @Column
    private String telephone;
    @Column
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user")
    @JsonBackReference(value = "user_registrations")
    private List<Registration> registrations;

}
