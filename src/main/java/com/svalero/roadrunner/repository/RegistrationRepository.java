package com.svalero.roadrunner.repository;

import com.svalero.roadrunner.domain.Race;
import com.svalero.roadrunner.domain.Registration;
import com.svalero.roadrunner.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findAll();
    List<Registration> findByRace(Race race);
    List<Registration> findByUser(User user);
    List<Registration> findByUserAndRace (User user, Race race);
}
