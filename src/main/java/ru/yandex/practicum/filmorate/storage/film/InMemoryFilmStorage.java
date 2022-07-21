package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film film) {
        Validator.filmValidate(film);
        film.setId(getNextId());
        log.info(String.valueOf(globalId));
        films.put(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        if (films.containsKey(film.getId())) {
            Validator.filmValidate(film);
            films.put(film.getId(), film);
            return film;
        } else throw new NoFoundException();
    }

    public List<Film> findPopular(int count) {
        ArrayList<Film> filmsSort = new ArrayList(films.values());
        Collections.sort(filmsSort, (o1, o2) -> {
            if (o1.getLikes().size() > o2.getLikes().size()) return 1;
            else if (o1.getLikes().size() < o2.getLikes().size()) return -1;
            else return 0;
        });
        return filmsSort.subList(0, count);
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }

}
