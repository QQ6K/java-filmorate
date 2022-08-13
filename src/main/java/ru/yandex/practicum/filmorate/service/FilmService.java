package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(DbFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film getFilm(int id) {
        checkId(id);
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        checkId(film.getId());
        if (!filmStorage.checkId(film.getId())) {
            throw new NoFoundException("Фильма не существует");
        }
        filmStorage.update(film);
        filmStorage.updateFilmMpa(film);
        filmStorage.updateFilmGenres(film);
        return getFilm(film.getId());
    }

    public Film addLikeToFilm(int id, Long userId) {
        filmStorage.checkId(id);
        filmStorage.addLikes(id, userId);
        return filmStorage.getFilm(id);
    }

    public Film deleteLikeFromFilm(int id, Long userId) {
        checkId(id);
        checkUser(userId);
        checkLike(id, userId);
        filmStorage.deleteLike(id, userId);
        return getFilm(id);
    }

    public List<Film> getPopular(int count) {
        if (count <= 0) throw new NoFoundException("Количество фильмов в выборке меньше или равно нулю");
        return filmStorage.findPopular(count);
    }

    private void checkId(int id) {
        if (!filmStorage.checkId(id)) {
            throw new NoFoundException("Отсутствует фильм с id = " + id);
        }
    }

    private void checkUser(Long userId) {
        if (!filmStorage.checkUser(userId)) {
            throw new NoFoundException("Фильм: Отсутствует пользователь с id = " + userId);
        }
    }

    private void checkLike(int id, Long userId) {
        if (!filmStorage.checkLike(id, userId)) {
            throw new NoFoundException("Отсутствует лайка!");
        }
    }

}
