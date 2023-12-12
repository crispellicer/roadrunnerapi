package com.svalero.roadrunner.service;

import com.svalero.roadrunner.domain.Race;
import com.svalero.roadrunner.exeption.RaceNotFoundException;

import java.util.List;

public interface RaceService {

    List<Race> findAll();
    List<Race> findByCity(String city);
    Race findById(long id) throws RaceNotFoundException;
    Race addRace (Race race);
    void deleteRace(long id) throws RaceNotFoundException;
    Race modifyRace(long id, Race newRace) throws RaceNotFoundException;
}
