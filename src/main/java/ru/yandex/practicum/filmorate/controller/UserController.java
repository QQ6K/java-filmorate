package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.Collection;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен GET запрос на /users");
        return userService.getUserStorage().findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен POST запрос на /users");
        Validator.userValidate(user);
        userService.getUserStorage().create(user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.info("Получен PUT запрос на /users");
        Validator.userValidate(user);
        userService.getUserStorage().put(user);
        return user;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }
}
