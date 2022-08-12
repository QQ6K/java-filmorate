package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(DbUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public User getUser(int id) {
        checkUserId(id);
        return userStorage.getUser(id);
    }

    public User create(User user) {
        checkUserExist(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        checkUserId(user.getId());
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public List<User> getUsersFriends(int id) {
        List<User> friends = new ArrayList<>();
        checkUserId(id);
        for (Long i : userStorage.getUser(id).getFriends()) {
            friends.add(userStorage.getUser(i.intValue()));
        }
        return friends;
    }

    public User addToFriends(int id, Long friendId) {
        checkUserId(id);
        checkUserId(friendId.intValue());

        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId.intValue());

        if (user.getFriends() == null) {
            userStorage.addFriend(id, friendId.intValue(), false);
        } else if (user.getFriends().contains(friendId)) {
            throw new ValidationException("Существует друг с id = " + friendId);
        }
        if (friend.getFriends() != null) {
            if (friend.getFriends().contains(id)) {
                userStorage.addFriend(id, friendId.intValue(), true);
            } else userStorage.addFriend(id, friendId.intValue(), false);
        }
        return userStorage.getUser(id);
    }

    public User removeFromFriends(int id, Long friendId) {
        checkUserId(id);
        checkUserId(friendId.intValue());

        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId.intValue());

        if (user.getFriends() != null) {
            userStorage.removeFromFriends(id, friendId.intValue());
        }

        if (friend.getFriends() != null && friend.getFriends().contains(id)) {
            userStorage.addFriend(friendId.intValue(), id, false);
        }
        return userStorage.getUser(id);
    }

    public List<User> getAllFriends(int id) {
        List friends = new ArrayList<>();
        checkUserId(id);
        for (long i : userStorage.getUsers().get(id).getFriends()) {
            friends.add(userStorage.getUsers().get(id));
        }
        return friends;
    }

    public ArrayList<User> getCommonFriends(int id, int friendId) {
        ArrayList<User> commonFriends = new ArrayList<>();
        checkUserId(id);
        checkUserId(friendId);
        HashSet<Long> usersFriends = userStorage.getUser(id).getFriends();
        HashSet<Long> friendFriends = userStorage.getUser(friendId).getFriends();
        if (usersFriends == null || friendFriends == null) {
            return new ArrayList<>();
        } else {
            for (Long i : usersFriends) {
                for (Long j : friendFriends) {
                    if (i.equals(j) && !commonFriends.contains(userStorage.getUser(i.intValue()))) {
                        commonFriends.add(userStorage.getUser(i.intValue()));
                    }
                }
            }
        }
        return commonFriends;
    }


    private void checkUserId(int id) {
        if (!userStorage.checkId(id)) {
            throw new NoFoundException("Отсутствует пользователь с id = " + id);
        }
    }

    private void checkUserExist(User user) {
        if (userStorage.checkName(user.getLogin()))
            throw new ValidationException("Пользователь с таким логином существует!");
        if (userStorage.checkEmail(user.getEmail()))
            throw new ValidationException("Пользователь с такой почтой существует!");
    }


}
