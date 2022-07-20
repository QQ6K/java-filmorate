package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {
    public User getUser(int id);
    public HashMap<Integer, User> getUsers();
    public Collection<User> findAll();
    public User create(User user);
    public User put(User user);
}
