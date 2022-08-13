package ru.yandex.practicum.filmorate.dbFilmorate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTests {

    private final DbUserStorage userStorage;

    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testCreateUser() throws JsonProcessingException {
        String json = "{\"email\":\"friend@mail.ru\"," +
                "\"login\":\"friend\",\"name\":\"friendadipisicing\"," +
                "\"birthday\":\"1976-08-20\",\"friends\":[2]}";
        User user = mapper.readValue(json, User.class);
        assertThat(userStorage.create(user)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testUpdateFilm() throws JsonProcessingException {
        testCreateUser();
        String json = "{\"email\":\"fr234234234iend@mail.ru\"," +
                "\"login\":\"friend\",\"name\":\"friend123123adipisicing\"," +
                "\"birthday\":\"1976-08-20\",\"friends\":[2]}";
        User user = mapper.readValue(json, User.class);
        user.setId(1);
        userStorage.update(user);
        assertThat(userStorage.getUser(1))
                .hasFieldOrPropertyWithValue("name", "friend123123adipisicing");
    }

    @Test
    public void testFindFilmById() throws JsonProcessingException {
        testCreateUser();
        Optional<User> filmOptional = Optional.ofNullable(userStorage.getUser(1));
        assertThat(filmOptional).isPresent().hasValueSatisfying(
                user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testFindAll() throws JsonProcessingException {
        String json = "{\"name\":\"UPDATE\",\"releaseDate\":\"1979-04-17\"," +
                "\"description\":\"Duisinconsequatesse\",\"duration\":100," +
                "\"rate\":4,\"mpa\":{\"id\":1}}";
        User user1 = mapper.readValue(json, User.class);

        json = "{\"name\":\"UPDATE\",\"releaseDate\":\"1979-04-17\"," +
                "\"description\":\"Duisinconsequatesse\",\"duration\":100," +
                "\"rate\":4,\"mpa\":{\"id\":1}}";
        User user2 = mapper.readValue(json, User.class);

        json = "{\"name\":\"UPDATE\",\"releaseDate\":\"1979-04-17\"," +
                "\"description\":\"Duisinconsequatesse\",\"duration\":100," +
                "\"rate\":4,\"mpa\":{\"id\":1}}";
        User user3 = mapper.readValue(json, User.class);

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        List<User> usersStart = new ArrayList<>();
        List<User> usersFin = new ArrayList<>();
        usersStart.add(user1);
        usersStart.add(user2);
        usersStart.add(user3);
        usersFin = (List<User>) userStorage.findAll();
        assertEquals(usersFin.size(), 3, "Размер выборки отличается");
        assertEquals(usersFin, usersStart, "Cостав выборки отличается");
        System.out.println(usersStart);
        System.out.println(usersFin);
    }
}
