package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

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

    public Film getFilm(int id) {
        checkFilmId(id);
        return filmStorage.getFilm(id);
    }

    public Film addLikeToFilm(int id, Long userId) {
        checkFilmUserIdForAdd(id, userId);
        filmStorage.getFilm(id).getLikes().add(userId);
        return filmStorage.getFilms().get(id);
    }

    public Film deleteLikeFromFilm(int id, Long userId) {
        checkFilmUserIdForDelete(id, userId);
        filmStorage.getFilm(id).getLikes().remove(userId);
        return filmStorage.getFilms().get(id);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.findPopular(count);
    }

    public void checkFilmId(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NoFoundException("Отсутствует фильм с id = " + id);
        }
    }

    public void checkFilmUserIdForDelete(int id, Long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NoFoundException("Отсутствует фильм с id = " + id);
        } else if (!filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new NoFoundException("Отсутствует лайк пользователя с userId = " + id);
        }
    }

    public void checkFilmUserIdForAdd(int id, Long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NoFoundException("Отсутствиет фильм с id = " + id);
        } else if (filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new NoFoundException("Пользователь с userId = " + id + " уже поставил лайк");
        }
    }


}
