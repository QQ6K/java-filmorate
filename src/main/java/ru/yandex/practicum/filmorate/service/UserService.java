package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public User AddToFriends(int id, Long friendId){
        userStorage.getUsers().get(id).addFriend(friendId);
        userStorage.getUsers().get(friendId).addFriend((long) id);
        return userStorage.getUsers().get(id);
    }

    public User DeleteFromFriends(int id, Long friendId){
        userStorage.getUsers().get(id).deleteFriend(friendId);
        userStorage.getUsers().get(friendId).addFriend((long) id);
        return userStorage.getUsers().get(id);
    }

    public List<User> getAllFriends(int id){
        List friends = new ArrayList<>();
        for (long i: userStorage.getUsers().get(id).getFriends()){
            friends.add(userStorage.getUsers().get(id));
        }
        return friends;
    }

       public ArrayList<Long> getMutualFriends(int id, int otherId){
        ArrayList<Long> mutualFriends = new ArrayList<>();
        for (Long i: userStorage.getUsers().get(id).getFriends()){
            for (Long j: userStorage.getUsers().get(otherId).getFriends()){
                if (i==j && !mutualFriends.contains(i)) {
                    mutualFriends.add(i);
                }
            }
        }
        return mutualFriends;
    }


}
