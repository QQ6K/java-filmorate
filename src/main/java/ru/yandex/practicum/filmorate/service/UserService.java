package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
    public User AddToFriends(@RequestBody User user, @PathVariable int id, @PathVariable int friendId){
        return user;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User DeleteFromFriends(@RequestBody User user, @PathVariable int id, @PathVariable int friendId){

        return user;
    }

    @GetMapping("/users/{id}/friends")
    public User getAllFriends(@RequestBody User user, @PathVariable int id, @PathVariable int friendId){
        return user;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public User getOtherUserFriends(@RequestBody User user, @PathVariable int id, @PathVariable int friendId){
        return user;
    }
}
