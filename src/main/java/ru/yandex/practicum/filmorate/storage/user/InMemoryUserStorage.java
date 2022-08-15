package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private final HashMap<Integer, User> users = new HashMap<>();
    private int globalId = 0;


    @Autowired
    private int getNextId() {
        return globalId++;
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public List<User> findAll() {
        return (List<User>) users.values();
    }

    public User create(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        Validator.userValidate(user);
        if (user.getId()==0) user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }


    public User update(User user) {
        return null;
    }

    @Override
    public boolean checkName(String name) {
        return false;
    }

    @Override
    public boolean checkEmail(String email) {
        return false;
    }

    @Override
    public boolean checkId(int id) {
        return false;
    }

    @Override
    public Set<Long> getFriends(int id) {
        return null;
    }

    @Override
    public void addFriend(int id, int friendId, boolean status) {

    }

    @Override
    public void removeFromFriends(int user_id, int friend_id) {

    }

    public User put(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        if (!userExist(user.getId())) {
            throw new NoFoundException("Пользователь не найден");
        }
        Validator.userValidate(user);
        users.put(user.getId(), user);
        return user;
    }

    
    public boolean userExist(int id) {
        return users.containsKey(id);
    }
}
