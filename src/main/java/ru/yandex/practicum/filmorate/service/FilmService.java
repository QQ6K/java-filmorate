package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistValidationException;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
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

    public Film createFilm(Film film){
        filmStorage.create(film);
        return film;
    }

    public Film getFilm(int id) {
        if (filmStorage.checkId(id)) {
            throw new ValidationException("Фильм существует");
        }
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film){
        if (filmStorage.getFilm(film.getId())==null) {
           throw new NoFoundException("Отсутсвует фильм с id = " + film.getId());
        }
        if (!filmStorage.checkId(film.getId())) {
            throw new NoFoundException("Фильма не существует");
        }
        filmStorage.update(film);
        filmStorage.updateFilmMpa(film);
        filmStorage.updateFilmGenres(film);
        return film;
    }

    public Film addLikeToFilm(int id, Long userId) {
        filmStorage.checkId(id);
        filmStorage.addLikes(id, userId);
        return filmStorage.getFilm(id);
    }

    public Film deleteLikeFromFilm(int id, Long userId) {
        checkFilmUserIdForDelete(id, userId);
        filmStorage.getFilm(id).getLikes().remove(userId);
        return filmStorage.getFilms().get(id);
    }

    public List<Film> getPopular(int count) {
        if (count<=0) throw new  NoFoundException("Количество фильмов в выборке меньше или равно нулю");
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
            throw new NoFoundException("Отсутствует фильм с id = " + id);
        } else if (filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new AlreadyExistValidationException("Пользователь с userId = " + userId + " уже поставил лайк");
        }
    }

}
