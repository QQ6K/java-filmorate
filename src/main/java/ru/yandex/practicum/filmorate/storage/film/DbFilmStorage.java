package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@Slf4j
@Component
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
                ("SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION  FROM FILMS where ID= ?", id);
       // List<Film> result = jdbcTemplate.query(sql, this::mapToFilm, id);
        film.setId(Integer.parseInt(filmRows.getString("id")));
        film.setName(filmRows.getString("name"));
        film.setDescription(filmRows.getString("description"));
        film.setReleaseDate(LocalDate.parse(filmRows.getString("RELEASE_DATE")));
        film.setId(Integer.parseInt(filmRows.getString("DURATION")));
        SqlRowSet rateRows = jdbcTemplate.queryForRowSet
                ("SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION  FROM FILMS where ID= ?", id);


       /* if (result.isEmpty()) {
            return null;
        }
        return result.get(0);*/
        return null;
    }



    @Override
    public Map<Integer, Film> getFilms() {
        return null;
    }

    @Override
    public Collection<Film> findAll() {
        return null;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film put(Film film) {
        return null;
    }

    @Override
    public List<Film> findPopular(int count) {
        return null;
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
  private Film mapToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(resultSet.getInt("DURATION"));
      //  film.setMpa(new Rating(resultSet.getLong("RATING_ID"), resultSet.getString("R_NAME")));
        return film;
    }


}
