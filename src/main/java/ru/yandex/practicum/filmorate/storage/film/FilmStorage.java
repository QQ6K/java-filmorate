package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    public Map<Integer, Film> getFilms();
    public Collection<Film> findAll();
    public Film create(@RequestBody Film film);
    public Film put(@RequestBody Film film);
    public List<Film> findPopular(int count);
}
