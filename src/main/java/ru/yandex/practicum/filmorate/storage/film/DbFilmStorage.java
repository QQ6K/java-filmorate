package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.utilities.QueriesStaticStrings.*;

@Slf4j
@Component
@Qualifier
@Primary
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(int id) {
        Film film = jdbcTemplate.query(getAllColumnsFilmsById, this::mapToFilm, id).get(0);
        film.setGenres(new HashSet<>(jdbcTemplate.query(getAllFilmGenresById, this::mapToGenre, id)));
        film.setMpa(jdbcTemplate.query(getFilmMpaById, this::mapToMpa, id).get(0));
        film.setLikes(new HashSet(jdbcTemplate.query(getUserIdFromFilmLikesByFilmId, this::mapToLike, id)));
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        List<Integer> filmIds = jdbcTemplate.query(getFilmIds, this::mapId);
        Collection<Film> films = Collections.emptySet();
        for (Integer id : filmIds) {
            films.add(getFilm(id));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate());
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        values.clear();

        if (film.getGenres() != null) {
            simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film_genres")
                    .usingColumns("film_id", "genre_id");
            for (Genre genre : film.getGenres()) {
                values.put("film_id", film.getId());
                values.put("genre_id", genre.getId());
                simpleJdbcInsert.execute(values);
            }
            values.clear();
        }

        if (film.getMpa() != null) {
            simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film_mpa")
                    .usingColumns("mpa_id", "film_id");
            values.put("mpa_id", film.getMpa().getId());
            values.put("film_id", film.getId());
            simpleJdbcInsert.execute(values);
        }

        return getFilm(film.getId());
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(updateAllFilmColumns, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> findPopular(int count) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet
                (getPopularFilms, count);
        List<Film> popularFilms = new ArrayList<>(Collections.emptyList());
        while (filmRows.next()) {
            Film film = getFilm(filmRows.getInt("id"));
            popularFilms.add(film);
        }
        return popularFilms;
    }

    @Override
    public void addLikes(int film_id, Long user_id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getUserLikeToFilmFromLikes, film_id, user_id);
        if (sqlRowSet.next()) throw new ValidationException("Фильм уже нравится! Сильнее он нравиться не может");

        jdbcTemplate.update(insertNewLike, film_id, user_id);
    }

    public void getLikes(Film film) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getUserIdFromFilmLikesByFilmId, film.getId());
        while (sqlRowSet.next()) {
            film.getLikes().add(sqlRowSet.getLong("user_id"));
        }
    }

    public void addFilmGenres(Film film) {
        Collection<Genre> genres = film.getGenres();
        if (genres == null) {
            return;
        }
        for (Genre genre : genres) {
            jdbcTemplate.update(insertFilmGenreToFilmGenres, film.getId(), genre.getId());
        }
    }

    @Override
    public void updateFilmGenres(Film film) {
        jdbcTemplate.update(deleteGenresFromFilmGenresByFilmID, film.getId());
        addFilmGenres(film);
    }

    public void addFilmMpa(Film film) {
        Mpa mpa = film.getMpa();
        jdbcTemplate.update(insertFilmMpa, film.getId(), mpa.getId());
    }

    public void updateFilmMpa(Film film) {
        jdbcTemplate.update(deleteFilmFromFilmMpa, film.getId());
        addFilmMpa(film);
    }

    @Override
    public boolean checkId(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getAllColumnsFilmsById, id);
        if (sqlRowSet.next()) return true;
        return false;
    }

    @Override
    public boolean checkUser(Long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getAllColumnsUserById, id);
        if (sqlRowSet.next()) return true;
        return false;
    }

    @Override
    public boolean checkLike(int id, Long userId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getUserLikeToFilmFromLikes, id, userId);
        if (sqlRowSet.next()) return true;
        return false;
    }

    @Override
    public boolean deleteLike(int id, Long userId) {
        jdbcTemplate.update(deleteLike, id, userId);
        return false;
    }

    private Film mapToFilm(ResultSet resultSet, int id) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(LocalDate.parse(resultSet.getString("release_date")));
        film.setDuration(resultSet.getInt("duration"));
        return film;
    }

    private Genre mapToGenre(ResultSet resultSet, int id) throws SQLException {
        Genre genre = new Genre();
        genre.setName(resultSet.getString("name"));
        genre.setId(resultSet.getInt("genre_id"));
        return genre;
    }

    private Mpa mapToMpa(ResultSet resultSet, int id) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setName(resultSet.getString("name"));
        mpa.setId(resultSet.getInt("mpa_id"));
        return mpa;
    }

    private Long mapToLike(ResultSet resultSet, int id) throws SQLException {
        return resultSet.getLong("user_id");
    }

    private int mapId(ResultSet resultSet, int id) throws SQLException {
        return resultSet.getInt("id");
    }

}
