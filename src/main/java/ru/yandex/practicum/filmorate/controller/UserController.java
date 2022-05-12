package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен GET запрос на /users");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
            log.info("Получен POST запрос на /users");
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new ValidationException("Адрес электронной почты не может быть пустым.");
            }
            if (!user.getEmail().contains("@")) {
                throw new ValidationException("Адрес электронной почты не может быть без символа @.");
            }
            if (users.containsKey(user.getEmail())) {
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
            if (user.getLogin() == null || user.getLogin().isEmpty()) {
                throw new ValidationException("Логин не может быть пустым.");
            }
            if (user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может содержать пробелы.");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Логин не может содержать пробелы.");
            }
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
            log.info("Получен PUT запрос на /users");
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new ValidationException("Адрес электронной почты не может быть пустым.");
            }
            if (!user.getEmail().contains("@")) {
                throw new ValidationException("Адрес электронной почты не может быть без символа @.");
            }
            if (users.containsKey(user.getEmail())) {
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
            if (user.getLogin() == null || user.getLogin().isEmpty()) {
                throw new ValidationException("Логин не может быть пустым.");
            }
            if (user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может содержать пробелы.");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Логин не может содержать пробелы.");
            }
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e){
        log.info(e.getMessage());
    }
}
