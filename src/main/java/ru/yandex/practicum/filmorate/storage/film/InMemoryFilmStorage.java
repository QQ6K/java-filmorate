package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int globalId = 0;

    @Autowired
    private int getNextId() {
        log.info(String.valueOf(globalId));
        return globalId++;
    }

    public Film getFilm(int id) {
            return films.get(id);
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film film) {
        Validator.filmValidate(film);
        if (film.getId()==0) film.setId(getNextId());
        log.info(String.valueOf(globalId));
        films.put(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        if (films.containsKey(film.getId())) {
            Validator.filmValidate(film);
            films.put(film.getId(), film);
            return film;
        } else throw new NoFoundException("Фильм не найден");
    }

    public List<Film> findPopular(int count) {
        ArrayList<Film> filmsSort = new ArrayList(films.values());
        if (!films.isEmpty()) {
            Collections.sort(filmsSort, (o2, o1) -> {
                if (o1.getLikes().size() > o2.getLikes().size()) return 1;
                else if (o1.getLikes().size() < o2.getLikes().size()) return -1;
                else return 0;
            });
            if (filmsSort.size() >= count) {
                return filmsSort.subList(0, count);
            } else return filmsSort;
        } else return filmsSort;
    }

}
