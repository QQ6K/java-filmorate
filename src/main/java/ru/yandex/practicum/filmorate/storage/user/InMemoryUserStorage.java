package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int globalId = 0;


    @Autowired
    private int getNextId() {
        return globalId++;
    }

    public User getUser(int id) {
        if (!users.containsKey(id)) throw new NoFoundException();
        return users.get(id);
    }

    public void addToFriends(int id, Long friendId) {
        if (!users.get(id).getFriends().contains(id) &&
                !users.get(id).getFriends().contains((int) (long) friendId)){
            users.get(id).getFriends().add(friendId);
        users.get(friendId.intValue()).getFriends().add((long) id);}
    }

    public void removeFromFriends(int id, Long friendId) {
        if (!users.get(id).getFriends().contains(id) &&
                !users.get(id).getFriends().contains((int) (long) friendId)){
        users.get(id).getFriends().remove(friendId);
        users.get((int) (long) friendId).getFriends().remove((long) id);}
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        Validator.userValidate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User put(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        if (!userExist(user.getId())) {
            throw new NoFoundException();
        }
        Validator.userValidate(user);
        users.put(user.getId(), user);
        return user;
    }

    public boolean userExist(int id) {
        return users.containsKey(id);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }
}
