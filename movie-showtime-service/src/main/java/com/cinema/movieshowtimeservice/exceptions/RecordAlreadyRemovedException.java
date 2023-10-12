package com.cinema.movieshowtimeservice.exceptions;

public class RecordAlreadyRemovedException extends RuntimeException{

    public RecordAlreadyRemovedException(String entity, String id) {
        super(entity + " with id:" + id + " is already removed!");
    }

}
