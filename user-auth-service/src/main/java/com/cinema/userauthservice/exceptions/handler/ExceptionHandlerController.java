package com.cinema.userauthservice.exceptions.handler;

import com.cinema.userauthservice.exceptions.IllegalStateExceptionExtension;
import com.cinema.userauthservice.exceptions.RecordNotFoundException;
import com.cinema.userauthservice.utils.JsonBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @<code>ExceptionHandlerController</code> - Controller for handling exceptions that occur in the methods of user and employee
 * service methods.They will be intercepted here and wrapped in certain response wrappers.
 */

@SuppressWarnings("all")
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * These are the exceptions that occur when constraints are violated in incoming request DTO objects.
     * @param exception
     * @return Map of all constraints that are violated in the request wrapped in BAD REQUEST status response.
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleClientRequestExceptions(MethodArgumentNotValidException exception) {
        JsonBuilder builder = new JsonBuilder();
        builder.put("timestamp", Date.from(Instant.now()).toString());
        for (ObjectError error: exception.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            builder.put(fieldName, errorMessage);
        }
        return builder.build();
    }

    /**
     * These are the exceptions that occur when constraints are violated in entity persistance
     * and exceptions that occur when server error is made while trying to contact other services using rest template
     * @param exception
     * @return Map where timestamp was put when the exception occured
     * and concrete error that happend when entity was persisted, the map is wrapped in INTERNAL SERVER ERROR status response.
     */
    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpServerErrorException.class,
            ResourceAccessException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handleConstraintViolationExceptions(ConstraintViolationException exception) {
        JsonBuilder builder = new JsonBuilder();
        return builder
                .put("timestamp", Date.from(Instant.now()).toString())
                .put("error", exception.getMessage())
                .build();
    }

    /**
     * These are the exceptions that occur when a record is not found in database while executing a certain logic in method.
     * @see RecordNotFoundException
     * @param exception
     * @return Map where timestamp was put when the exception occured
     * and concrete error message of what was not found, the map is wrapped in NOT FOUND status response.
     */
    @ExceptionHandler({
            RecordNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,Object> handleCustomExceptions(Exception exception) {
        JsonBuilder builder = new JsonBuilder();
        return builder
                .put("timestamp", Date.from(Instant.now()).toString())
                .put("error", exception.getMessage())
                .build();
    }

    /**
     * These are the exceptions that occur when a certain logic error occurs in a user or employee service method.
     * Logic error is a forbidden program flow,such as a cashier employee can't have a manager that is also a cashier
     * and exceptions that occur when client error is made while trying to contact other services using rest template
     * @exception IllegalStateExceptionExtension
     * @param exception
     * @return Map where timestamp was put when the exception occured
     * and concrete error message of what bad happend in service method, the map is wrapped in BAD REQUEST status response.
     */
    @ExceptionHandler({
            IllegalStateExceptionExtension.class,
            HttpClientErrorException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleCustomExceptions2(Exception exception) {
        JsonBuilder builder = new JsonBuilder();
        return builder
                .put("timestamp", Date.from(Instant.now()).toString())
                .put("error", exception.getMessage())
                .build();
    }

}
