package ru.yandex.practicum.filmorate.interfaces;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    Film getFilm(int id);

    Map<Integer, Film> getFilms();

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    List<Film> findPopular(int count);

    void updateFilmMpa(Film film);

    void addLikes(int film_id, Long user_id);

    void updateFilmGenres(Film film);

    boolean checkId(int id);

    boolean checkUser(Long id);

    boolean checkLike(int id, Long userId);

    boolean deleteLike(int id, Long userId);
}
