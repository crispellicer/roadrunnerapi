package com.svalero.roadrunner.service;

import com.svalero.roadrunner.domain.User;
import com.svalero.roadrunner.exeption.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(long id) throws UserNotFoundException;
    List<User> findByName(String name);
    User addUser (User user);
    void deleteUser (long id) throws UserNotFoundException;
    User modifyUser(long id, User newUser) throws UserNotFoundException;
}
