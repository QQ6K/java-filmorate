package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

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
        Film film = new Film();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet
                ("SELECT * FROM FILMS where ID= ?");
        if (filmRows.next()) {
            film.setId(Integer.parseInt(filmRows.getString("id")));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(LocalDate.parse(filmRows.getString("release_date")));
            film.setDuration(Integer.parseInt(filmRows.getString("duration")));
        }
        return film;
    }


    @Override
    public Map<Integer, Film> getFilms() {
       return null;
    }

    @Override
    public Collection<Film> findAll() {
        String sql =
                "SELECT * FROM films";
        return
                jdbcTemplate.query(sql, new ResultSetExtractor<Collection<Film>>() {
                    @Override
                    public Collection<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        Collection<Film> films = new ArrayList<>();
                        while(rs.next()){
                            films.add(MapFilms(rs));
                        }
                        return films;
                    }
                });
    }

    private Film MapFilms(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        return film;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return film;
    }

    @Override
    public Film put(Film film) {
        String sql =
                "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ? WHERE ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> findPopular(int count) {
        /*Validator.filmValidate(film);
        //if (film.getId()==0) film.setId(getNextId());
        log.info(String.valueOf("ok"));
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", film.getName());
        values.put("DESCRIPTION", "film.getDescription()");
        values.put("RELEASE_DATE", "2020-05-30");
        values.put("DURATION", "150");
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());

        return film;*/
        return null;
    }

    public void addLikes(Film film) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id  = ?", film.getId());
        String sql = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        Set<Long> likes = film.getLikes();
        for (Long id : likes ) {
            jdbcTemplate.update(sql, film.getId(), id);
        }
    }

    public void getLikes(Film film) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (sqlRowSet.next()) {
            film.getLikes().add(sqlRowSet.getLong("user_id"));
        }

    /*  @Override
      public Film create(Film film) {
          SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                  .withTableName("FILMS")
                  .usingGeneratedKeyColumns("FILM_ID");

          Map<String, Object> valuesFilm = new HashMap<>();
          Map<String, Object> valuesRate = new HashMap<>();
          valuesFilm.put("NAME", film.getName());
          valuesFilm.put("DESCRIPTION", film.getDescription());
          valuesFilm.put("RELEASE_DATE", film.getReleaseDate());
          valuesFilm.put("DURATION", film.getDuration());
       //  valuesRate.put()

          film.setId(simpleJdbcInsert.executeAndReturnKey(valuesFilm).intValue());


          return film;
      }


      */
    /*private Film mapToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(resultSet.getInt("DURATION"));
        //  film.setMpa(new Rating(resultSet.getLong("RATING_ID"), resultSet.getString("R_NAME")));
        return film;*/
    }

    public void addFilmGenres(Film film) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES(?, ?)";
        Set<Genre> genres = film.getGenres();
        if (genres == null) {
            return;
        }
        for (var genre : genres ) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }}



        public void updateFilmGenres(Film film) {
            String sql = "DELETE FROM film_genres WHERE film_id= ?";
            jdbcTemplate.update(sql, film.getId());
            addFilmGenres(film);
        }

    public void addFilmMpa(Film film) {
        String sql = "INSERT INTO film_mpa (film_id, mpa_id) VALUES(?, ?)";
        Mpa mpa = film.getMpa();
            jdbcTemplate.update(sql, film.getId(), mpa.getId());
        }



    public void updateFilmMpa(Film film) {
        String sql = "DELETE FROM film_genres WHERE film_id= ?";
        jdbcTemplate.update(sql, film.getId());
        addFilmGenres(film);
    }


}
