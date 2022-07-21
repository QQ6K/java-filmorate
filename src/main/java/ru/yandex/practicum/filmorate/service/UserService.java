package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
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

    public User getUser(int id){
        return userStorage.getUser(id);
    }

    public List<User> getUsersFriends(int id){
        List<User> friends = new ArrayList<>();
        if (!userStorage.getUser(id).getFriends().isEmpty()){
        for (Long i:userStorage.getUser(id).getFriends()){
            friends.add(userStorage.getUser((int)(long)i));
        }}
        return friends;
    }

    public User addToFriends(int id, Long friendId){
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId.intValue())){
        userStorage.addToFriends(id,friendId);
        userStorage.addToFriends((int)(long)friendId,(long)id);
        return userStorage.getUsers().get(id);}
        else throw new NoFoundException();
    }

    public User removeFromFriends(int id, Long friendId){
        userStorage.removeFromFriends(id,friendId);
        userStorage.removeFromFriends((int)(long)friendId,(long)id);
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
