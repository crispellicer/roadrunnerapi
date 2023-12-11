package com.svalero.roadrunner.controller;

import com.svalero.roadrunner.domain.Race;
import com.svalero.roadrunner.exeption.ErrorMessage;
import com.svalero.roadrunner.exeption.RaceNotFoundException;
import com.svalero.roadrunner.service.RaceService;
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
public class RaceController {

    @Autowired
    RaceService raceService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @GetMapping("/races")
    public ResponseEntity<List<Race>> getRaces(@RequestParam(name = "city", defaultValue = "") String city) {
        logger.debug("begin getRaces");
        if (city.equals("")) {
            logger.debug("end getRaces");
            return ResponseEntity.ok(raceService.findAll());
        } else {
            logger.debug("end getRaces");
            return ResponseEntity.ok(raceService.findByCity(city));
        }
    }

    @GetMapping("/races/{id}")
    public ResponseEntity<Race> getRace(@PathVariable String id) throws RaceNotFoundException, NumberFormatException {
        logger.debug("begin getRace");
        if (!id.matches("[0-9]*")) {
            throw new NumberFormatException();
        }
        long raceId = Long.parseLong(id);
        Race race = raceService.findById(raceId);
        logger.debug("end getRace");
        return ResponseEntity.ok(race);
    }

    @PostMapping("/races")
    public ResponseEntity<Race> addRace(@Valid @RequestBody Race race) {
        logger.debug("begin addRace");
        Race newRace = raceService.addRace(race);
        logger.debug("end addRace");
        return ResponseEntity.status(HttpStatus.CREATED).body(newRace);

    }

    @DeleteMapping ("/races/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable long id) throws RaceNotFoundException {
        raceService.deleteRace(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/races/{id}")
    public ResponseEntity<Race> modifyRace(@PathVariable long id, @Valid @RequestBody Race race) throws RaceNotFoundException {
        logger.debug("begin modifyRace");
        Race modifiedRace = raceService.modifyRace(id, race);
        logger.debug("end modifyRace");
        return ResponseEntity.status(HttpStatus.OK).body(modifiedRace);
    }

    @ExceptionHandler(RaceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRaceNotFoundException(RaceNotFoundException rnfe) {
        logger.error(rnfe.getMessage(), rnfe);
        ErrorMessage errorMessage = new ErrorMessage(404, rnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
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
