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
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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
        while (filmRows.next()) {
            film.setId(Integer.parseInt(filmRows.getString("id")));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(LocalDate.parse(filmRows.getString("release_date")));
            film.setDuration(Integer.parseInt(filmRows.getString("duration")));
        }

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet
                ("SELECT fg.genre_id id, gn.name as name FROM film_genres fg " +
                        "LEFT JOIN genre_names gn ON fg.genre_id = gn.id WHERE fg.film_id = ?", id);
        System.out.println(genreRows.getRow());
        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre();
        while (genreRows.next()) {
            genre.setName(genreRows.getString("name"));
            genre.setId(Integer.parseInt(genreRows.getString("genre_id")));
            genres.add(genre);
        }

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet
                ("SELECT mpa_id, name FROM film_mpa fm " +
                        "join mpa_names mn on fm.mpa_id = mn.id where film_id = ?", id);
        Mpa mpa = new Mpa();
        if (mpaRows.next()) {
            mpa.setId(Integer.parseInt(mpaRows.getString("mpa_id")));
            mpa.setName(mpaRows.getString("name"));
        }

        SqlRowSet likeRows = jdbcTemplate.queryForRowSet
                ("SELECT user_id FROM likes where film_id = ?", id);
        Set likes = new HashSet();
        if (likeRows.next()) {
            likes.add(Long.valueOf(likeRows.getString("user_id")));
        }

        film.setGenres(genres);
        film.setMpa(mpa);
        film.setLikes(likes);
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
                        while (rs.next()) {
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
                .usingColumns("film_id","genre_id");
        for (Genre genre: film.getGenres()) {
            values.put("film_id", film.getId());
            values.put("genre_id",genre.getId());
            simpleJdbcInsert.execute(values);
        }
        values.clear();}

        if (film.getMpa() != null) {
            simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film_mpa")
                    .usingColumns("mpa_id", "film_id");
            values.put("mpa_id", film.getMpa().getId());
            values.put("film_id", film.getId());
            simpleJdbcInsert.execute(values);
        }

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
                ("select ID, count(USER_ID) as count FROM FILMS " +
                        "left join LIKES L on FILMS.ID = L.FILM_ID group by id limit ?", count);

        List popularFilms = new ArrayList();
        Film film;
        int film_id;
        int countMark;
        while (filmRows.next()) {
            film_id = Integer.valueOf(filmRows.getString("id"));
           // countMark = Integer.valueOf(filmRows.getString("count"));
            film = getFilm(Integer.valueOf(filmRows.getString("id")));
         //   film.setRate(countMark);
            popularFilms.add(film);
        }
        return popularFilms;

    }

    @Override
    public void addLikes(int film_id, Long user_id) {
        String sql =
                "SELECT * FROM likes WHERE (film_id = ? AND user_id = ?)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film_id, user_id);
        if (sqlRowSet.next()) throw new ValidationException("Фильм уже нравится! Сильнее он нравиться не может");
        sql = "INSERT INTO likes(film_id, user_id) VALUES(?, ?)";
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
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }


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
    public boolean checkId(int id) {
        String sql =
                "SELECT * FROM films WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) return true;
        return false;
    }

    @Override
    public boolean checkUser(Long id) {
        String sql =
                "SELECT * FROM users WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) return true;
        return false;
    }

    @Override
    public boolean checkLike(int id, Long userId) {
        String sql =
                "SELECT * FROM likes WHERE film_id = ? AND user_id = ? ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id, userId);
        if (sqlRowSet.next()) return true;
        return false;
    }

    @Override
    public boolean deleteLike(int id, Long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE (film_id = ? AND user_id=?)",id,userId);
        return false;
    }



}
