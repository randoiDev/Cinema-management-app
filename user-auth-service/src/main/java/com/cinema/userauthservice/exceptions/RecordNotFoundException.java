package com.cinema.userauthservice.exceptions;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String entity, String id) {
        super(entity + " with id:" + id + " doesn't exist, please try again!");
    }

}
