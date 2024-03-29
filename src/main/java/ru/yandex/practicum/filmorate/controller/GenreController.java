package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres/{id}")
    public Genre findById(@PathVariable int id) {
        return genreService.findById(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @PostMapping("/genre")
    public Genre create(@RequestBody Genre genre) {
        return genreService.create(genre);
    }

    @PutMapping("/genre")
    public Genre update(@RequestBody Genre genre) {
        return genreService.update(genre);
    }
}
