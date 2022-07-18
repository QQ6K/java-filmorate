package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {
    public HashMap<Integer, User> getUsers();
    public Collection<User> findAll();
    public User create(@RequestBody User user);
    public User put(@RequestBody User user);
}
