package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;


@Slf4j
@RestController
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("(/mpa/{id}")
    public Mpa findById(@PathVariable int id) {
        return mpaService.findById(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> findAll() {
        return mpaService.findAll();
    }

    @PostMapping("/mpa")
    public Mpa create(@RequestBody Mpa mpa) {
        return mpaService.create(mpa);
    }

    @PutMapping("/mpa")
    public Mpa update(@RequestBody Mpa mpa) {
        return mpaService.update(mpa);
    }
}
