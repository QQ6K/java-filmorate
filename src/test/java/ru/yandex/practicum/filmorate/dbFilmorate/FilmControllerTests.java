package ru.yandex.practicum.filmorate.dbFilmorate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTests {

    private final DbFilmStorage filmStorage;
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testCreateFilm() throws JSONException, JsonProcessingException {
        String json =
                "{\"name\":\"laborenulla\",\"releaseDate\":\"1979-04-17\"," +
                        "\"description\":\"Duisinconsequatesse\",\"duration\":100," +
                        "\"rate\":4,\"mpa\":{\"id\":1}}";
        Film film = mapper.readValue(json, Film.class);
        assertThat(filmStorage.create(film)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testUpdateFilm() throws JSONException, JsonProcessingException {
        testCreateFilm();
        String json =
                "{\"name\":\"UPDATE\",\"releaseDate\":\"1979-04-17\"," +
                        "\"description\":\"Duisinconsequatesse\",\"duration\":100," +
                        "\"rate\":4,\"mpa\":{\"id\":1}}";
        Film film = mapper.readValue(json, Film.class);
        film.setId(1);
        filmStorage.update(film);
        assertThat(filmStorage.getFilm(1)).hasFieldOrPropertyWithValue("name", "UPDATE");
    }

    @Test
    public void testFindFilmById() throws JSONException, JsonProcessingException {
        testCreateFilm();
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAll() throws JSONException, JsonProcessingException {
        String json =
                "{\"name\":\"laasdasdaulla\",\"description\":\"Duisasdasdesse\"," +
                        "\"releaseDate\":\"1979-04-17\",\"duration\":100,\"likes\":[],\"mpa\":{\"id\":1," +
                        "\"name\":\"G\"},\"genres\":[],\"rate\":0}";
        Film film1 = mapper.readValue(json, Film.class);

        json = "{\"name\":\"asdaываываsdalla\",\"description\":\"Dasdasdquatesse\"," +
                "\"releaseDate\":\"1999-04-17\",\"duration\":100,\"likes\":[],\"mpa\":{\"id\":1," +
                "\"name\":\"G\"},\"genres\":[],\"rate\":0}";
        Film film2 = mapper.readValue(json, Film.class);

        json =   "{\"name\":\"laborenulla\",\"description\":\"Duiasdasdnsequatesse\"," +
                "\"releaseDate\":\"2000-04-17\",\"duration\":100,\"likes\":[],\"mpa\":{\"id\":1," +
                "\"name\":\"G\"},\"genres\":[],\"rate\":0}";
        Film film3 = mapper.readValue(json, Film.class);

        filmStorage.create(film1);
        filmStorage.create(film2);
        filmStorage.create(film3);
        film1.setId(1);film2.setId(2);film3.setId(3);
        List<Film> filmsStart = new ArrayList<>();
        List<Film> filmsFin = new ArrayList<>();
        filmsStart.add(film1); filmsStart.add(film2); filmsStart.add(film3);
        filmsFin = (List<Film>) filmStorage.findAll();
        assertEquals(filmsFin.size(),3, "Размер выборки отличается");
        assertEquals(filmsStart.containsAll(filmsStart), true,"Cостав выборки отличается");
        System.out.println(filmsStart);
        System.out.println(filmsFin);
    }



}

