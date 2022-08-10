package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;


@Slf4j
@RestController
public class MpaController {

    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable int id) {
        log.info("Получен GET запрос на /mpa/" + id);
        return mpaService.getMpa(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> findAll() {
        log.info("Получен GET запрос на /mpa");
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
