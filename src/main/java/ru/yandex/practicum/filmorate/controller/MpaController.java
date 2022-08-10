package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.MpaService;


@Slf4j
@RestController
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("(/mpa/{id}")
    public Genre findById(@PathVariable int id) {
        return mpaService.findById(id);
    }

    @GetMapping("/mpa")
    public Collection<E> findAll() {
        return mpaService.findAll();
    }

    @PostMapping("/mpa")
    public E create(@RequestBody E data) {
        return mpaService.create(data);
    }

    @PutMapping("/mpa")
    public E update(@RequestBody E data) {
        return mpaService.update(data);
    }
}
