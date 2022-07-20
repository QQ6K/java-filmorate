package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Получен GET запрос на /users");
        return userService.getUserStorage().findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Получен POST запрос на /users");
        Validator.userValidate(user);
        userService.getUserStorage().create(user);
        return user;
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {
        log.info("Получен PUT запрос на /users");
        Validator.userValidate(user);
        userService.getUserStorage().put(user);
        return user;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id){
        log.info("Получен GET запрос на /users/id");
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User AddToFriends(@PathVariable int id, @PathVariable Long friendId){
        return userService.AddToFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List getAllFriends(@PathVariable int id){
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ArrayList<Long> getOtherUserFriends(@PathVariable int id, @PathVariable int otherId){
        return userService.getMutualFriends(id, otherId);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoHandlerFound(NoHandlerFoundException e)  {
        log.info(e.getMessage());
    }

    @ExceptionHandler(InternalError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleExceptionISE(Exception e)  {
        log.info(e.getMessage());
    }

}
