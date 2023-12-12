package com.svalero.roadrunner.repository;

import com.svalero.roadrunner.domain.Race;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends CrudRepository<Race, Long> {

    List<Race> findAll();
    List<Race> findByCity(String city);
}
