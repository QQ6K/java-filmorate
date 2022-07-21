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

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }


    public Film addLikeToFilm(int id, Long userId) {
        if (filmStorage.getFilms().containsKey(id)) {
            if (!filmStorage.getFilms().get(id).getLikes().contains(userId)) {
                filmStorage.getFilms().get(id).getLikes().add(userId);
            }
        }
        return filmStorage.getFilms().get(id);
    }

    public Film deleteLikeFromFilm(int id, Long userId) {
        if (filmStorage.getFilms().containsKey(id)) {
            if (filmStorage.getFilms().get(id).getLikes().contains(userId)) {
                filmStorage.getFilms().get(id).getLikes().remove(userId);
            }
        }
        return filmStorage.getFilms().get(id);
    }


    public List<Film> getPopular(int count) {
        return filmStorage.findPopular(count);
    }

}
