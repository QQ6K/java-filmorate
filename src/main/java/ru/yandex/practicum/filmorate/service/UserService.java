package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistValidationException;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.ArrayList;
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
        checkIdUser(id);
        return userStorage.getUser(id);
    }

    public List<User> getUsersFriends(int id) {
        List<User> friends = new ArrayList<>();
        checkIdUser(id);
        for (Long i : userStorage.getUser(id).getFriends()) {
            friends.add(userStorage.getUser(i.intValue()));
        }
        return friends;
    }

    public User addToFriends(int id, Long friendId) {
        checkIdUser(friendId.intValue());
        checkUserFriendIdAdd(id, friendId);
        userStorage.getUser(id).getFriends().add(friendId);
        userStorage.getUser(friendId.intValue()).getFriends().add((long)id);
        return userStorage.getUsers().get(id);
    }

    public User removeFromFriends(int id, Long friendId) {
        checkUserFriendIdDelete(id, friendId);
        userStorage.getUser(id).getFriends().remove(friendId);
        userStorage.getUser(friendId.intValue()).getFriends().remove((long)id);
        return userStorage.getUsers().get(id);
    }

    public List<User> getAllFriends(int id) {
        List friends = new ArrayList<>();
        checkIdUser(id);
        for (long i : userStorage.getUsers().get(id).getFriends()) {
            friends.add(userStorage.getUsers().get(id));
        }
        return friends;
    }

    public ArrayList<User> getCommonFriends(int id, int otherId) {
        ArrayList<User> commonFriends = new ArrayList<>();
        checkIdUser(id);
        checkIdUser(otherId);
        for (Long i : userStorage.getUser(id).getFriends()) {
            for (Long j : userStorage.getUser(otherId).getFriends()) {
                if (i.equals(j) && !commonFriends.contains(userStorage.getUser(i.intValue()))) {
                    commonFriends.add(userStorage.getUser(i.intValue()));
                }
            }
        }
        return commonFriends;
    }

    private void checkIdUser(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NoFoundException("Отсутствиет пользователь с id = " + id);
        }
    }

    private void checkUserFriendIdAdd(int id, Long userId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NoFoundException("Отсутствиет пользователь с id = " + id);
        } else if (userStorage.getUsers().get(id).getFriends().contains(userId)) {
            throw new AlreadyExistValidationException("В списке друзей уже есть пользователь с userId = " + id);
        }
    }

    private void checkUserFriendIdDelete(int id, Long userId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NoFoundException("Отсутствиет пользователь с id = " + id);
        } else if (!userStorage.getUsers().get(id).getFriends().contains(userId)) {
            throw new NoFoundException("В списке друзей нет пользователя с userId = " + id);
        }
    }


}
