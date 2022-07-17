package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private final int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Long> likes;

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration, HashSet<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
    }

    public void addLike(Long id) {
        this.likes.add(id);
    }

    public void deleteLike(Long id) {
        this.likes.remove(id);
    }
}
