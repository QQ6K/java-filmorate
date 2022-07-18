package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
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

    @PutMapping("/users/{id}/friends/{friendId}")
    public User AddToFriends(@RequestBody User user, @PathVariable int id, @PathVariable Long friendId){
        userStorage.getUsers().get(id).addFriend(friendId);
        userStorage.getUsers().get(friendId).addFriend((long) id);
        return user;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User DeleteFromFriends(@RequestBody User user, @PathVariable int id, @PathVariable Long friendId){
        userStorage.getUsers().get(id).deleteFriend(friendId);
        userStorage.getUsers().get(friendId).addFriend((long) id);
        return user;
    }

    @GetMapping("/users/{id}/friends")
    public Set<Long> getAllFriends(@PathVariable int id){
        return userStorage.getUsers().get(id).getFriends();
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ArrayList<Long> getOtherUserFriends(@PathVariable int id, @PathVariable int otherId){
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
