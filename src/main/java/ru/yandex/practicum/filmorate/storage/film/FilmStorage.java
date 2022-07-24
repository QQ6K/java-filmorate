package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film getFilm(int id);

    Map<Integer, Film> getFilms();

    Collection<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    List<Film> findPopular(int count);
}
