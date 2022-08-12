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
                ("SELECT * FROM films where id= ?", id);
        if (filmRows.next()) {
            film.setId(Integer.parseInt(filmRows.getString("id")));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(LocalDate.parse(filmRows.getString("release_date")));
            film.setDuration(Integer.parseInt(filmRows.getString("duration")));
        }
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet
                ("SELECT * FROM film_genres where film_id = ?", id);
        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre();
        if (genreRows.next()){
            genre.setId(Integer.parseInt(genreRows.getString("genre_id")));
            SqlRowSet genreName = jdbcTemplate.queryForRowSet
                    ("SELECT * FROM genre_names where id= ?", genre.getId());
            genre.setName(genreName.getString("name"));
            genres.add(genre);
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
                .usingGeneratedKeyColumns("id");
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate",film.getRate());
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql =
                "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ? WHERE ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> findPopular(int count) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet
                ("select FILM_ID, count(FILM_ID) as count from LIKES GROUP BY FILM_ID LIMIT ?", count);

        List popularFilms = new ArrayList();
        Film film = new Film();
        int film_id;
        int countMark;
        if (filmRows.next()) {
            film_id = Integer.valueOf(filmRows.getString("film_id"));
            countMark =  Integer.valueOf(filmRows.getString("count"));
            film = getFilm(film_id);
            film.setRate(countMark);
            popularFilms.add(film);
        }
        if (popularFilms == null) {
            popularFilms = List.of(findAll());
        }
        return popularFilms;

    }

    @Override
    public void addLikes(int film_id, Long user_id) {
        String sql = "INSERT INTO likes(film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, film_id, user_id);
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
        for (Genre genre : genres ) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }}


    @Override
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

    @Override
    public void updateFilmMpa(Film film) {
        String sql = "DELETE FROM film_mpa WHERE film_id= ?";
        jdbcTemplate.update(sql, film.getId());
        addFilmMpa(film);
    }

    @Override
    public boolean checkId(int id){
        String sql =
                "SELECT * FROM films WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) return true;
        return false;
    }

}
