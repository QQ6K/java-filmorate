package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {

    User getUser(int id);

    HashMap<Integer, User> getUsers();

    Collection<User> findAll();

    User create(User user);

    User put(User user);
}
