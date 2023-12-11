package com.svalero.roadrunner.exeption;

public class RaceNotFoundException extends Exception {

    public RaceNotFoundException(String message) {
        super(message);
    }

    public RaceNotFoundException() {
        super("Race not found");
    }
}
