package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/films")
@Slf4j
@RestController
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public Collection<Film> findAll() {
       return films.values();
    }

    public Film create(@RequestBody Film film) {
        Validator.filmValidate(film);
        films.put(film.getId(), film);
        return film;
    }

    public Film put(@RequestBody Film film) {
        Validator.filmValidate(film);
        films.put(film.getId(), film);
        return film;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }

}
