package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
class FilmControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getFilmControllerResponseShouldBeOkTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/films");
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createValidFilmResponseShouldBeOkTest() throws Exception {
        Film film = new Film(1, "Простоквашино",
                "О несчастных и счастливых, " +
                        "о добре и зле, о лютой ненависти и святой любви",
                LocalDate.of(1978, 6, 6), 19);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createFilmBeforeBirthTest() throws Exception {
        Film film = new Film(2, "L'arroseur arrosé",
                "Политый поливальщик",
                LocalDate.of(1895, 12, 27), 2);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createFilmBirthTest() throws Exception {
        Film film = new Film(3, "L'arroseur arrosé",
                "Политый поливальщик",
                LocalDate.of(1895, 12, 28), 2);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createFilmAfterBirthTest() throws Exception {
        Film film = new Film(4, "L'arroseur arrosé",
                "Политый поливальщик",
                LocalDate.of(1895, 12, 29), 2);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createFilmNegativeDurationTest() throws Exception {
        Film film = new Film(5, "Нечто",
                "Группа учёных сообщила о падении летающего объекта",
                LocalDate.of(1982, 6, 25), -1);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createFilmZeroDurationTest() throws Exception {
        Film film = new Film(6, "Нечто",
                "Группа учёных сообщила о падении летающего объекта",
                LocalDate.of(1982, 6, 25), 0);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createFilmPositiveDurationTest() throws Exception {
        Film film = new Film(7, "Нечто",
                "Группа учёных сообщила о падении летающего объекта",
                LocalDate.of(1982, 6, 25), 1);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createValidFilmNameIsEmptyTest() throws Exception {
        Film film = new Film(8, "",
                "Based on his own fascination with spirituality, " +
                        "Aykroyd conceived Ghostbusters as a project starring " +
                        "himself and John Belushi, in which they would space " +
                        "battling supernatural threats.",
                LocalDate.of(1984, 6, 8), 105);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createValidFilmNameIsNullTest() throws Exception {
        Film film = new Film(9, null,
                "Based on his own fascination with spirituality, " +
                        "Aykroyd conceived Ghostbusters as a project starring " +
                        "himself and John Belushi, in which they would space " +
                        "battling supernatural threats.",
                LocalDate.of(1984, 6, 8), 105);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createValidFilmDescription199Test() throws Exception {
        Film film = new Film(10, "Back to the future",
                "Back to the Future is a 1985 American science" +
                        " fiction film. The story follows Marty McFly (Fox)," +
                        " a teenager accidentally sent back to 1955 in " +
                        "a time-traveling automobile. Directed by Robert Zemeckis.",
                LocalDate.of(1985, 7, 3), 116);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
        assertEquals(body, result.getResponse().getContentAsString(),
                "Данные Film-post и Film-return не совпадают");
    }

    @Test
    void createValidFilmDescription200Test() throws Exception {
        Film film = new Film(11, "Back to the future",
                "Back to the Future is a 1985 American science" +
                        " fiction film. The story follows Marty McFly (Fox)," +
                        " a teenager accidentally sent back to 1955 in " +
                        "a time-traveling automobile. Directed by Robert Zemeckis!!",
                LocalDate.of(1985, 7, 3), 116);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
        assertEquals(body, result.getResponse().getContentAsString(),
                "Данные Film-post и Film-return не совпадают");
    }

    @Test
    void createValidFilmDescription201Test() throws Exception {
        Film film = new Film(12, "Back to the future",
                "Back to the Future is a 1985 American science" +
                        " fiction film. The story follows Marty McFly (Fox)," +
                        " a teenager accidentally sent back to 1955 in " +
                        "a time-traveling automobile. Directed by Robert Zemeckis!!!",
                LocalDate.of(1985, 7, 3), 116);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createValidFilmPutTest() throws Exception {
        Film film = new Film(13, "Back",
                "Back!",
                LocalDate.of(1990, 1, 2), 12);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void getFilmValidTest() throws Exception {
        Film film = new Film(14, "Back",
                "Back!",
                LocalDate.of(1990, 1, 2), 123);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        request = MockMvcRequestBuilders.get("/films/14")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
        assertEquals(body, result.getResponse().getContentAsString(),
                "Данные Film-post и Film-return не совпадают");
    }

    @Test
    void getFilmInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/films/-1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }

    @Test
    void addFilmLikeTest() throws Exception {
        Film film = new Film(131, "Back",
                "Back!",
                LocalDate.of(1990, 1, 2), 123);
        String body131 = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body131)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        User user130 = new User(130, "test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body130 = mapper.writeValueAsString(user130);
        request = MockMvcRequestBuilders.post("/users")
                .content(body130)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        request = MockMvcRequestBuilders.put("/films/131/like/130")
                .content(body130)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");

        //проверка 409
        request = MockMvcRequestBuilders.put("/films/131/like/130")
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andReturn();
        assertEquals(409, result.getResponse().getStatus(), "Ожидался код ответа 409");
    }

    @Test
    void deleteFilmLikeValidTest() throws Exception {
        Film film = new Film(132, "Back",
                "Back!",
                LocalDate.of(1990, 1, 2), 123);
        String body132 = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body132)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        User user13201 = new User(13201, "test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body13201 = mapper.writeValueAsString(user13201);
        request = MockMvcRequestBuilders.post("/users")
                .content(body13201)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        request = MockMvcRequestBuilders.put("/films/132/like/13201")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");

        request = MockMvcRequestBuilders.delete("/films/132/like/13201")
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");

        assertEquals(body132, result.getResponse().getContentAsString(),
                "Данные Film-post и Film-return не совпадают");

        request = MockMvcRequestBuilders.delete("/films/132/like/13201")
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }

    @Test
    void deleteFilmLikeInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/films/131/like/13")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }


    @Test
    void getPopularTest() throws Exception {
        Film film = new Film(300, "300", "300",
                LocalDate.of(1978, 6, 6), 19);
        film.getLikes().add(1L);
        film.getLikes().add(2L);
        film.getLikes().add(3L);
        String body = mapper.writeValueAsString(film);
        RequestBuilder request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(100, "100", "100",
                LocalDate.of(1978, 6, 6), 19);
        film.getLikes().add(1L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();


        film = new Film(101, "101", "101",
                LocalDate.of(1978, 6, 6), 19);
        film.getLikes().add(445L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(200, "200", "200",
                LocalDate.of(1978, 6, 6), 19);
        film.getLikes().add(1L);
        film.getLikes().add(5L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(400, "400", "400",
                LocalDate.of(1978, 6, 6), 194);
        film.getLikes().add(1L);
        film.getLikes().add(5L);
        film.getLikes().add(52L);
        film.getLikes().add(51L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(500, "500", "500",
                LocalDate.of(1978, 6, 6), 195);
        film.getLikes().add(1L);
        film.getLikes().add(6L);
        film.getLikes().add(10L);
        film.getLikes().add(22L);
        film.getLikes().add(45L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(600, "600", "600",
                LocalDate.of(1978, 6, 6), 196);
        film.getLikes().add(1L);
        film.getLikes().add(6L);
        film.getLikes().add(10L);
        film.getLikes().add(22L);
        film.getLikes().add(45L);
        film.getLikes().add(425L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(601, "601", "601",
                LocalDate.of(1978, 6, 6), 196);
        film.getLikes().add(1L);
        film.getLikes().add(6L);
        film.getLikes().add(10L);
        film.getLikes().add(22L);
        film.getLikes().add(45L);
        film.getLikes().add(425L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(602, "602", "602",
                LocalDate.of(1978, 6, 6), 196);
        film.getLikes().add(1L);
        film.getLikes().add(6L);
        film.getLikes().add(10L);
        film.getLikes().add(22L);
        film.getLikes().add(45L);
        film.getLikes().add(425L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(700, "700", "700",
                LocalDate.of(1978, 6, 6), 197);
        film.getLikes().add(1L);
        film.getLikes().add(6L);
        film.getLikes().add(10L);
        film.getLikes().add(22L);
        film.getLikes().add(45L);
        film.getLikes().add(425L);
        film.getLikes().add(415L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(800, "800", "800",
                LocalDate.of(1978, 6, 6), 198);
        film.getLikes().add(1L);
        film.getLikes().add(6L);
        film.getLikes().add(10L);
        film.getLikes().add(22L);
        film.getLikes().add(45L);
        film.getLikes().add(425L);
        film.getLikes().add(415L);
        film.getLikes().add(4445L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(900, "900", "900",
                LocalDate.of(1978, 6, 6), 199);
        film.getLikes().add(1L);
        film.getLikes().add(2L);
        film.getLikes().add(3L);
        film.getLikes().add(4L);
        film.getLikes().add(5L);
        film.getLikes().add(6L);
        film.getLikes().add(7L);
        film.getLikes().add(8L);
        film.getLikes().add(9L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        film = new Film(1000, "1000", "1000",
                LocalDate.of(1978, 6, 6), 1910);
        film.getLikes().add(1L);
        film.getLikes().add(2L);
        film.getLikes().add(3L);
        film.getLikes().add(4L);
        film.getLikes().add(5L);
        film.getLikes().add(6L);
        film.getLikes().add(7L);
        film.getLikes().add(8L);
        film.getLikes().add(9L);
        film.getLikes().add(10L);
        body = mapper.writeValueAsString(film);
        request = MockMvcRequestBuilders.post("/films")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();

        request = MockMvcRequestBuilders.get("/films/popular")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void getPopular2Test() throws Exception {
        getPopularTest();
        RequestBuilder request = MockMvcRequestBuilders.get("/films/popular?count=2")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
        String res = "[{\"id\":1000,\"name\":\"1000\",\"description\":\"1000\",\"releaseDate\":\"1978-06-06\"," +
                "\"duration\":19,\"likes\":[1,2,3,4,5,6,7,8,9,10],\"rate\":10},{\"id\":900,\"name\":\"900\"," +
                "\"description\":\"900\",\"releaseDate\":\"1978-06-06\",\"duration\":19,\"likes\":[1,2,3,4,5,6,7,8,9]," +
                "\"rate\":9}]";
        assertEquals(result.getResponse().getContentAsString(), res,"Не совпадает длина выборки" );
    }

    @Test
    void getPopularInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/films/popular?count=-3")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }

}
