package com.svalero.roadrunner.service;

import com.svalero.roadrunner.domain.Race;
import com.svalero.roadrunner.domain.User;
import com.svalero.roadrunner.exeption.RaceNotFoundException;
import com.svalero.roadrunner.repository.RaceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceServiceImpl implements RaceService{

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Race> findAll() {
        return raceRepository.findAll();
    }

    @Override
    public List<Race> findByCity(String city) {
        return raceRepository.findByCity(city);
    }

    @Override
    public Race findById(long id) throws RaceNotFoundException {
        return raceRepository.findById(id)
                .orElseThrow(RaceNotFoundException::new);
    }

    @Override
    public Race addRace(Race race) {
        return raceRepository.save(race);
    }


    @Override
    public void deleteRace(long id) throws RaceNotFoundException {
        Race race = raceRepository.findById(id)
                .orElseThrow(RaceNotFoundException::new);
        raceRepository.delete(race);
    }

    @Override
    public Race modifyRace(long id, Race newRace) throws RaceNotFoundException {
        Race existingRace = raceRepository.findById(id)
                .orElseThrow(RaceNotFoundException::new);
        existingRace.setName(newRace.getName());
        existingRace.setDistance(newRace.getDistance());
        existingRace.setType(newRace.getType());
        existingRace.setCity(newRace.getCity());
        existingRace.setRegistrationPrice(newRace.getRegistrationPrice());
        existingRace.setRaceDate(newRace.getRaceDate());
        existingRace.setLongitude(newRace.getLongitude());
        existingRace.setLatitude(newRace.getLatitude());

        return raceRepository.save(existingRace);
    }
}
