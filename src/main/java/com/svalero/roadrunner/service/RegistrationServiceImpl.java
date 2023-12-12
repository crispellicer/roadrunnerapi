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
import com.svalero.roadrunner.repository.RaceRepository;
import com.svalero.roadrunner.repository.RegistrationRepository;
import com.svalero.roadrunner.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService{

    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Registration> findAll() {
        return registrationRepository.findAll();
    }


    @Override
    public Registration findById(long id) throws RegistrationNotFoundException {
        return registrationRepository.findById(id)
                .orElseThrow(RegistrationNotFoundException::new);
    }

    @Override
    public Registration addRegistration(RegistrationInDTO registrationInDTO, long raceId) throws RaceNotFoundException, UserNotFoundException, UserAlreadyInARaceException {
        Registration newRegistration = new Registration();
        modelMapper.map(registrationInDTO, newRegistration);

        Race race = raceRepository.findById(raceId)
                .orElseThrow(RaceNotFoundException::new);
        newRegistration.setRace(race);

        User user = userRepository.findById(registrationInDTO.getUser())
                .orElseThrow(UserNotFoundException::new);
        newRegistration.setUser(user);

        List<Registration> registrations = registrationRepository.findByUserAndRace(user, race);

        if (registrations.isEmpty()) {
            return registrationRepository.save(newRegistration);
        } else {
            throw new UserAlreadyInARaceException();
        }
    }

    @Override
    public void deleteRegistration(long registrationId) throws RegistrationNotFoundException {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(RegistrationNotFoundException::new);
        registrationRepository.delete(registration);
    }

    @Override
    public List<Registration> findByRace(Race race) {
        return registrationRepository.findByRace(race);
    }

    @Override
    public List<RegistrationOutDTO> findByUser(User user) {
        List<Registration> registrations = registrationRepository.findByUser(user);
        List<RegistrationOutDTO> registrationsOutDTO = new ArrayList<>();
        for (Registration registration : registrations) {
            RegistrationOutDTO registrationOutDTO = new RegistrationOutDTO();
            registrationOutDTO.setId(registration.getId());
            registrationOutDTO.setRaceName(registration.getRace().getName());
            registrationOutDTO.setDistance(registration.getRace().getDistance());
            registrationOutDTO.setCity(registration.getRace().getCity());

            registrationsOutDTO.add(registrationOutDTO);
        }

        return registrationsOutDTO;
    }
}

