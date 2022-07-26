package ru.yandex.practicum.filmorate.exceptions;

public class AlreadyExistValidationException extends RuntimeException{
    public AlreadyExistValidationException(String s) {
        super(s);
    }
}
