package com.svalero.roadrunner.exeption;

public class RegistrationNotFoundException extends Exception {

    public RegistrationNotFoundException(String message) {
        super(message);
    }

    public RegistrationNotFoundException() {
        super("Registration not found");
    }
}
