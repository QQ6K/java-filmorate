package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreService {

    private final GenreStorage dbGenreStorage;

    @Autowired
    public GenreService(DbGenreStorage dbGenreStorage) {
        this.dbGenreStorage = dbGenreStorage;
    }

    public Collection<Genre> findAll() {
        return dbGenreStorage.findAll();
    }

    public Genre create(Genre genre) {
        return dbGenreStorage.create(genre);
    }

    public Genre update(Genre genre) {
        return dbGenreStorage.update(genre);
    }

    public Genre findById(int id) {
        Genre genre = dbGenreStorage.getGenre(id);
        if (genre.getId() == 0 && genre.getName() == null) {
            log.warn("Отсутствует жанр с id=" + id);
            throw new NoFoundException("не удалось найти жанр");
        }

        return genre;
    }

}
