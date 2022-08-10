package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Genre getMpa(int id);

    Collection<Mpa> findAll();

    Genre create(Mpa mpa);

    Genre update(Mpa mpa);
}
