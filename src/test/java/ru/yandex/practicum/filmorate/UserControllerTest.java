package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
    void putUserIdFriendsInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/1/friends/8000")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }

    @Test
    void putUserIdInvalidFriendsInvalidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/500/friends/-5")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
    }

    @Test
    void putUserIdFriendsInternalErrorTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/500/friends/5.5")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(500, result.getResponse().getStatus(), "Ожидался код ответа 500");
    }

    @Test
    void putUserIdFriendsValidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/1/friends/2")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
    }

    @Test
    void putUserIdFriendsAgainValidTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/users/1/friends/2")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
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

  //  /users/{id}/friends/common/{otherId}
    void getUsersFriendsCommonFriendsTest() throws Exception {
        User user = new User(13,"test@test.ru",
                "test", "Stephan", LocalDate.of(2000, 11, 11));
        String body = mapper.writeValueAsString(user);
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();

    }

}
