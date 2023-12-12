package com.svalero.roadrunner.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationOutDTO {

    private long id;
    private String raceName;
    private String distance;
    private String city;

}
