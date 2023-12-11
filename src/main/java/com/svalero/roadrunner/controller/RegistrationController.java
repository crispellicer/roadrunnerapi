package com.svalero.roadrunner.controller;

import com.svalero.roadrunner.domain.Race;
import com.svalero.roadrunner.domain.Registration;
import com.svalero.roadrunner.domain.User;
import com.svalero.roadrunner.domain.dto.RegistrationInDTO;
import com.svalero.roadrunner.domain.dto.RegistrationOutDTO;
import com.svalero.roadrunner.exeption.*;
import com.svalero.roadrunner.service.RaceService;
import com.svalero.roadrunner.service.RegistrationService;
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

import java.lang.NumberFormatException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private RaceService raceService;

    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @GetMapping("/registrations")
    public ResponseEntity<List<Registration>> getRegistrations() {
        logger.debug("begin getRegistrations");
        logger.debug("end getRegistrations");
        return ResponseEntity.ok(registrationService.findAll());
    }

    @GetMapping("/races/{id}/registrations")
    public ResponseEntity<List<Registration>> getRegistrationsByRaceId(@PathVariable long id) throws RaceNotFoundException{
        Race race = raceService.findById(id);
        List<Registration> registrations = registrationService.findByRace(race);
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/users/{id}/registrations")
    public ResponseEntity<List<RegistrationOutDTO>> getRegistrationsByUserId(@PathVariable long id) throws UserNotFoundException{
        User user = userService.findById(id);
        List<RegistrationOutDTO> registrationsOutDTO = registrationService.findByUser(user);
        return ResponseEntity.ok(registrationsOutDTO);
    }

    @GetMapping("/registrations/{id}")
    public ResponseEntity<Registration> getRegistration(@PathVariable String id) throws RegistrationNotFoundException, NumberFormatException {
        logger.debug("begin getRegistration");
        if (!id.matches("[0-9]*")) {
            throw new NumberFormatException();
        }
        long registrationId = Long.parseLong(id);
        Registration Registration = registrationService.findById(registrationId);
        logger.debug("end getRegistration");
        return ResponseEntity.ok(Registration);
    }

    @PostMapping("/races/{id}/registrations")
    public ResponseEntity<Registration> addRegistration(@PathVariable long id, @Valid @RequestBody RegistrationInDTO registrationInDTO) throws RaceNotFoundException, UserNotFoundException, UserAlreadyInARaceException {
        logger.debug("begin addRegistration");
        Registration newRegistration = registrationService.addRegistration(registrationInDTO, id);
        logger.debug("end addRegistration");
        return ResponseEntity.status(HttpStatus.CREATED).body(newRegistration);
    }

    @DeleteMapping ("/registrations/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable long id) throws RegistrationNotFoundException {
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RegistrationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRegistrationNotFoundException(RegistrationNotFoundException rnfe) {
        logger.error(rnfe.getMessage(), rnfe);
        ErrorMessage errorMessage = new ErrorMessage(404, rnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RaceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRestaurantNotFoundException(RaceNotFoundException rnfe) {
        logger.error(rnfe.getMessage(), rnfe);
        ErrorMessage errorMessage = new ErrorMessage(404, rnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleCustomerNotFoundException(UserNotFoundException unfe) {
        logger.error(unfe.getMessage(), unfe);
        ErrorMessage errorMessage = new ErrorMessage(404, unfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyInARaceException.class)
    public ResponseEntity<ErrorMessage> handleAddressAlreadyInARestaurantException(UserAlreadyInARaceException uaiare) {
        logger.error(uaiare.getMessage(), uaiare);
        ErrorMessage errorMessage = new ErrorMessage(400, uaiare.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorMessage> handleFormatNumberException(NumberFormatException nfe) {
        logger.error(nfe.getMessage(), nfe);
        ErrorMessage errorMessage = new ErrorMessage(400, nfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        ErrorMessage badRequestErrorMessage = new ErrorMessage(400, "Bad request", errors);
        return new ResponseEntity<>(badRequestErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
