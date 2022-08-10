package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistValidationException;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
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
        if (count<=0) throw new  NoFoundException("Количество фильмов в выборке меньеш или равно нулю");
        return filmStorage.findPopular(count);
    }

    private void checkFilmId(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NoFoundException("Отсутствует фильм с id = " + id);
        }
    }

    private void checkFilmUserIdForDelete(int id, Long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NoFoundException("Отсутствует фильм с id = " + id);
        } else if (!filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new NoFoundException("Отсутствует лайк пользователя с userId = " + userId);
        }
    }

    private void checkFilmUserIdForAdd(int id, Long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NoFoundException("Отсутствиет фильм с id = " + id);
        } else if (filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new AlreadyExistValidationException("Пользователь с userId = " + userId + " уже поставил лайк");
        }
    }


}
