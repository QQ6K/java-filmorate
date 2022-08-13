package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        return userService.findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Получен POST запрос на /users");
        Validator.userValidate(user);
        userService.create(user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("Получен PUT запрос на /users");
        Validator.userValidate(user);
        userService.update(user);
        return user;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        log.info("Получен GET запрос на /users/id");
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable int id, @PathVariable Long friendId) {
        log.info("Получен PUT запрос на /users/" + id + "/friends/" + friendId.intValue());
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable int id, @PathVariable Long friendId) {
        log.info("Получен DELETE запрос на /users/" + id + "/friends/" + friendId.intValue());
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List getAllFriends(@PathVariable int id) {
        log.info("Получен GET запрос на /users/" + id + "/friends");
        return userService.getUsersFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{friendId}")
    public ArrayList<User> getOtherUserFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен GET запрос на /users/" + id + "/friends/common/" + friendId);
        return userService.getCommonFriends(id, friendId);
    }

}
