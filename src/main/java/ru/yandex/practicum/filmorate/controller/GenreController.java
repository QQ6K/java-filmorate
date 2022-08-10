package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("(/mpa/{id}")
    public Genre findById(@PathVariable int id) {
        return genreService.findById(id);
    }

    @GetMapping("/mpa")
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @PostMapping("/mpa")
    public Genre create(@RequestBody Genre genre) {
        return genreService.create(genre);
    }

    @PutMapping("/mpa")
    public Genre update(@RequestBody Genre genre) {
        return genreService.update(genre);
    }
}
