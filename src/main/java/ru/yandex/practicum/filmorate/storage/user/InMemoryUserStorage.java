package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Collection;
import java.util.HashMap;

import static org.springframework.http.HttpMethod.PUT;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private  int globalId = 0;


    @Autowired
    private int getNextId() {
        return globalId++;
    }

    public User getUser(int id) {
        if (!users.containsKey(id)) throw new NoFoundException();
        return users.get(id);
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
        if (!userExist(user.getId())){
            throw new NoFoundException();}
        Validator.userValidate(user);
        users.put(user.getId(), user);
        return user;
    }

    public boolean userExist(int id){
        return users.containsKey(id);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }
}
