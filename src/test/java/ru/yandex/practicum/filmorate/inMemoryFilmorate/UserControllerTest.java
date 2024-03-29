package ru.yandex.practicum.filmorate.inMemoryFilmorate;

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
import ru.yandex.practicum.filmorate.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getUserControllerEmptyDataResponseShouldBeOkTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users");
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void createValidUserResponseShouldBeOkTest() throws Exception {
        User user = new User(1,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
        assertEquals(body, result.getResponse().getContentAsString(),
                "Данные User-post и User-return не совпадают");
    }

    @Test
    void createUserEmailWithoutAtTest() throws Exception {
        User user = new User(2, "test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserEmailEmptyTest() throws Exception {
        User user = new User(3,"",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserEmailNullTest() throws Exception {
        User user = new User(4,null,
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserLoginEmptyTest() throws Exception {
        User user = new User(5,"test@test.ru",
                "", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserLoginNullTest() throws Exception {
        User user = new User(6,"test@test.ru",
                null, "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserLoginWithSpaceTest() throws Exception {
        User user = new User(7,"test@test.ru",
                "T shirt", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserLoginNameTest() throws Exception {
        User user = new User(8,"test@test.ru",
                "Tshirt", "", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserBirthdayTest() throws Exception {
        User user = new User(9,"test@test.ru",
                "Tshirt", "Георгий", LocalDate.now().plusDays(1));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void createUserPutTest() throws Exception {
        User user = new User(1,"test@test.ru",
                "Tshirt", "Георгий", LocalDate.now().minusDays(1));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.put("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void getUserValidTest() throws Exception {
        User user = new User(11,"test@test.ru",
                "Tshirt", "Георгий", LocalDate.now().minusDays(1));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        request = MockMvcRequestBuilders.get("/users/11")
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
        assertEquals(body, result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                "Данные User-post и User-return не совпадают");
    }

    @Test
    void getUserInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users/-1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }


    @Test
    void putUserIdInvalidFriendsTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/-1/friends/3")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }


    @Test
    void getUserIdFriendsInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/1/friends/8000")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }

    @Test
    void getUserIdInvalidFriendsInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/500/friends/-5")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }

    @Test
    void getUserIdFriendsInternalErrorTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/500/friends/5.5")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(500, result.getResponse().getStatus(), "Ожидался код ответа 500");
    }

    @Test
    void addUserIdFriendsValidTest() throws Exception {
        User user100 = new User(100,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body100 = mapper.writeValueAsString(user100);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body100)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();

        User user200 = new User(200,"test1100@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body200 = mapper.writeValueAsString(user200);
        request = MockMvcRequestBuilders.post("/users")
                .content(body200)
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andReturn();

        request = MockMvcRequestBuilders.put("/users/100/friends/200")
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
        //повторное добавление проверка 409
        request = MockMvcRequestBuilders.put("/users/100/friends/200")
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andReturn();
        assertEquals(409, result.getResponse().getStatus(), "Ожидался код ответа 409");
    }

    @Test
    void getUsersFriendsValidTest() throws Exception {
        User user = new User(12,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result1 = mockMvc.perform(request).andReturn();
        request = MockMvcRequestBuilders.get("/users/12/friends")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void getUsersFriendsNotValidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users/-1/friends")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }


    void getUsersFriendsCommonFriendsTest() throws Exception {
        User user1 = new User(1001,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body1 = mapper.writeValueAsString(user1);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body1)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        User user2 = new User(1002,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body2 = mapper.writeValueAsString(user2);
        request = MockMvcRequestBuilders.post("/users")
                .content(body2)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        User user3 = new User(1003,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body3 = mapper.writeValueAsString(user3);
        request = MockMvcRequestBuilders.post("/users")
                .content(body3)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        User user4 = new User(1004,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body4 = mapper.writeValueAsString(user4);
        request = MockMvcRequestBuilders.post("/users")
                .content(body4)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        //Добавление друзей
        request = MockMvcRequestBuilders.put("/users/1001/friends/1002")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        request = MockMvcRequestBuilders.put("/users/1001/friends/1003")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        request = MockMvcRequestBuilders.put("/users/1001/friends/1004")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        request = MockMvcRequestBuilders.put("/users/1002/friends/1004")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        //общие друзья
        request = MockMvcRequestBuilders.get("/users/1002/friends/common/1001")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andReturn();
        assertEquals(body4, result.getResponse().getContentAsString(),
                "Данные User-post и User-return не совпадают");
    }

}
