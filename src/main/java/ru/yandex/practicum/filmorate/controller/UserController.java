package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    User user = new User(1);
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        users.put(1,user);
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
