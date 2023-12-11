package com.svalero.roadrunner.controller;

import com.svalero.roadrunner.domain.User;
import com.svalero.roadrunner.exeption.ErrorMessage;
import com.svalero.roadrunner.exeption.UserNotFoundException;
import com.svalero.roadrunner.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "name", defaultValue = "") String name) {
        logger.debug("begin getUsers");
        if (name.equals("")) {
            logger.debug("end getUsers");
            return ResponseEntity.ok(userService.findAll());
        } else {
            logger.debug("end getUsers");
            return ResponseEntity.ok(userService.findByName(name));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) throws UserNotFoundException, NumberFormatException {
        logger.debug("begin getUser");
        if (!id.matches("[0-9]*")) {
            throw new NumberFormatException();
        }
        long userId = Long.parseLong(id);
        User user = userService.findById(userId);
        logger.debug("end getUser");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        logger.debug("begin addUser");
        User newUser = userService.addUser(user);
        logger.debug("end addUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> modifyUser(@PathVariable long id, @Valid @RequestBody User user) throws UserNotFoundException{
        logger.debug("begin modifyUser");
        User modifiedUser = userService.modifyUser(id, user);
        logger.debug("end modifyUser");
        return ResponseEntity.status(HttpStatus.OK).body(modifiedUser);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<com.svalero.roadrunner.exeption.ErrorMessage> handleRaceNotFoundException(UserNotFoundException unfe) {
        logger.error(unfe.getMessage(), unfe);
        com.svalero.roadrunner.exeption.ErrorMessage errorMessage = new ErrorMessage(404, unfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<com.svalero.roadrunner.exeption.ErrorMessage> handleFormatNumberException(NumberFormatException nfe) {
        logger.error(nfe.getMessage(), nfe);
        com.svalero.roadrunner.exeption.ErrorMessage errorMessage = new com.svalero.roadrunner.exeption.ErrorMessage(400, nfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.svalero.roadrunner.exeption.ErrorMessage> handleBadRequestException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        com.svalero.roadrunner.exeption.ErrorMessage badRequestErrorMessage = new com.svalero.roadrunner.exeption.ErrorMessage(400, "Bad request", errors);
        return new ResponseEntity<>(badRequestErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.svalero.roadrunner.exeption.ErrorMessage> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        com.svalero.roadrunner.exeption.ErrorMessage errorMessage = new com.svalero.roadrunner.exeption.ErrorMessage(500, "Internal Server Error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}