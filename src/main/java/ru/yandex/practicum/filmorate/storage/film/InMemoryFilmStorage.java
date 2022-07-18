package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utilities.Validator;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/films")
@Slf4j
@RestController
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public Collection<Film> findAll() {
       return films.values();
    }

    public Film create(Film film) {
        Validator.filmValidate(film);
        films.put(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        Validator.filmValidate(film);
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> findPopular(int count){
       ArrayList<Film> filmsSort = new ArrayList(films.values());
       Collections.sort(filmsSort, (o1, o2) -> {
           if (o1.getLikes().size()>o2.getLikes().size()) return 1;
           else if (o1.getLikes().size()<o2.getLikes().size()) return -1;
           else return 0;
       });
       return filmsSort.subList(0,count);
    }




    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ValidationException e) {
        log.info(e.getMessage());
    }

}
