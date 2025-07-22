package fr.afpa.restapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseEntity extends ResponseEntity<String> {

    public ErrorResponseEntity() {
        super("Ca va pas la tête ?", HttpStatus.BAD_REQUEST);
    }
}
