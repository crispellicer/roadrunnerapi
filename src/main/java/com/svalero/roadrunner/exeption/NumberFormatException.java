package com.svalero.roadrunner.exeption;

public class NumberFormatException extends Exception {

    public NumberFormatException(String message) {
        super(message);
    }

    public NumberFormatException() {
        super("You can only enter numbers");
    }
}