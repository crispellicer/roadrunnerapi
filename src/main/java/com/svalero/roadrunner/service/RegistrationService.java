package com.svalero.roadrunner.service;

import com.svalero.roadrunner.domain.Race;
import com.svalero.roadrunner.domain.Registration;
import com.svalero.roadrunner.domain.User;
import com.svalero.roadrunner.domain.dto.RegistrationInDTO;
import com.svalero.roadrunner.domain.dto.RegistrationOutDTO;
import com.svalero.roadrunner.exeption.RaceNotFoundException;
import com.svalero.roadrunner.exeption.RegistrationNotFoundException;
import com.svalero.roadrunner.exeption.UserAlreadyInARaceException;
import com.svalero.roadrunner.exeption.UserNotFoundException;

import java.util.List;

public interface RegistrationService {

    List<Registration> findAll();
    Registration findById(long id) throws RegistrationNotFoundException;
    List<Registration> findByRace(Race race);

    List<RegistrationOutDTO> findByUser(User user);
    Registration addRegistration (RegistrationInDTO registrationInDTO, long raceId) throws RaceNotFoundException, UserNotFoundException, UserAlreadyInARaceException;
    void deleteRegistration (long registrationId) throws RegistrationNotFoundException;
}
