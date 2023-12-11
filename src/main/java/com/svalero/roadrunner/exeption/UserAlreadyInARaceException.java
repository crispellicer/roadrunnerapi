package com.svalero.roadrunner.exeption;

public class UserAlreadyInARaceException extends Exception{

    public UserAlreadyInARaceException(String message) {
        super(message);
    }
    public UserAlreadyInARaceException() {
        super("User already registered in this race");
    }
}
