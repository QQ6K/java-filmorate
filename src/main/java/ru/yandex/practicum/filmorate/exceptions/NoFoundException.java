package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class NoFoundException extends ResponseStatusException {

    public NoFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}

