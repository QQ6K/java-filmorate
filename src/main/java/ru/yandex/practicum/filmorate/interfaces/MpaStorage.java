package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {

    Mpa getMpa(int id);

    Collection<Mpa> findAll();

    Mpa create(Mpa mpa);

    Mpa update(Mpa mpa);
}
