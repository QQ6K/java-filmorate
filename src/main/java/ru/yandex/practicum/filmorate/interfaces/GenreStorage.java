package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    Genre getGenre(int id);

    List<Genre> findAll();

    Genre create(Genre genre);

    Genre update(Genre genre);
}
