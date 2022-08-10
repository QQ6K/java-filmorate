package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre getGenre(int id);

    Collection<Genre> findAll();

    Genre create(Genre genre);

    Genre update(Genre genre);
}
