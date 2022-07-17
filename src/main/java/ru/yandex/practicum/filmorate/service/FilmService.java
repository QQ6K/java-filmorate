package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLikeToFilm(@RequestBody Film film, @PathVariable int id, @PathVariable Long userId){
        filmStorage.getFilms().get(id).addLike(userId);
        return film;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film getAllFriends(@RequestBody Film film, @PathVariable int id, @PathVariable Long userId){
        filmStorage.getFilms().get(id).deleteLike(userId);
        return film;
    }

    @GetMapping("/films/popular?count={count}")
    public Film getOtherUserFriends(@RequestBody Film film, @PathVariable int count){
        return film;
    }

}
