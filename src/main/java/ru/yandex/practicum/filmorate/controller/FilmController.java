package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RequestMapping("/films")
@Slf4j
@RestController
public class FilmController {
private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен GET запрос на /films");
        return filmService.getFilmStorage().findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен POST запрос на /films");
        filmService.getFilmStorage().put(film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        log.info("Получен PUT запрос на /films");
        filmService.getFilmStorage().put(film);
        return film;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }

}
