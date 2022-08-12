package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("Получен GET запрос на /films");
        return filmService.getFilmStorage().findAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.info("Получен POST запрос на /films");
        Validator.filmValidate(film);
        filmService.getFilmStorage().create(film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.info("Получен PUT запрос на /films");
        Validator.filmValidate(film);
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Получен GET запрос на /films/"+id);
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("Получен GET запрос на /films/popular");
        return filmService.getPopular(count);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable Long userId) {
        log.info("Получен DELETE запрос на /films/"+id +"/like/"+userId.intValue());
        return filmService.deleteLikeFromFilm(id, userId);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLikeToFilm(@PathVariable int id, @PathVariable Long userId) {
        log.info("Получен PUT запрос на /films/"+id +"/like/"+userId.intValue());
        return filmService.addLikeToFilm(id, userId);
    }




}
