package com.svalero.roadrunner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "races")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotBlank(message = "The field cannot be empty")
    @NotNull(message = "The field is required")
    private String name;
    @Column
    private String distance;
    @Column
    private String type;
    @Column
    private String city;
    @Column
    @Min(value = 0)
    private double registrationPrice;
    @Column
    private LocalDate raceDate;
    @Column
    private double longitude;
    @Column
    private double latitude;



    @OneToMany(mappedBy = "race")
    @JsonBackReference(value = "race_registrations")
    private List<Registration> registrations;
}
