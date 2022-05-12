package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {
    LocalDate cinemaBirthday = LocalDate.of(1895,12,28);
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен GET запрос на /films");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
            log.info("Получен POST запрос на /films");
            if (film.getName() == null || film.getName().isBlank()) {
                throw new ValidationException("Название фильма не может быть пустым.");
            }
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания — 200 символов.");
            }
            if (film.getDescription() == null || film.getDescription().isEmpty()) {
                throw new ValidationException("Описание фильма совершенно пустое!");
            }
            if (film.getReleaseDate().isBefore(cinemaBirthday)) {
                throw new ValidationException("Дата производства фильма до дня рождения кино!");
            }
            if (film.getDuration().isNegative() || film.getDuration().isZero()) {
                throw new ValidationException("Продолжительность фильма должна быть больше 0");
            }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        log.info("Получен PUT запрос на /films");
            if (film.getName() == null || film.getName().isBlank()) {
                throw new ValidationException("Название фильма не может быть пустым.");
            }
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания — 200 символов.");
            }
            if (film.getDescription() == null || film.getDescription().isEmpty()) {
                throw new ValidationException("Описание фильма совершенно пустое!");
            }
            if (film.getReleaseDate().isBefore(cinemaBirthday)) {
                throw new ValidationException("Дата производства фильма до дня рождения кино!");
            }
            if (film.getDuration().isNegative() || film.getDuration().isZero()) {
                throw new ValidationException("Продолжительность фильма должна быть больше 0");
            }
        films.put(film.getId(), film);
        return film;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e){
        log.info(e.getMessage());
    }
}
