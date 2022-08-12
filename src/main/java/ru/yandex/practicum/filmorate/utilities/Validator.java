package ru.yandex.practicum.filmorate.utilities;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {
    private static final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);

    public static void userValidate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты не может быть без символа @.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше текущей.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Имя было пустым, мы приняли волевое решение использовать логин как имя!");
        }
    }

    public static void filmValidate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getDescription() == null || film.getDescription().isEmpty()) {
            throw new ValidationException("Описание фильма совершенно пустое!");
        }
        if (film.getReleaseDate().isBefore(cinemaBirthday)) {
            throw new ValidationException("Дата производства фильма до дня рождения кино!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть больше 0!");
        }
        if (film.getMpa() == null) {
            throw new ValidationException("У фильма отсутсвует рейтинг!");
        }
    }
}
