package ru.gpb.middle_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.gpb.middle_service.backendMock.UserRepository;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.dto.CreateUserRequestV2;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        userRepository.save(new UserMock("111-222-333",2,"user2"));
    }

    @Test
    void userRegisterTest() throws Exception{
        CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2();
        createUserRequestV2.setUserId(1);
        createUserRequestV2.setUserName("user1");
        mockMvc.perform(post("/v2/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(createUserRequestV2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists());
        Optional<UserMock> userMock = userRepository.findByTelegramId(1);
        Assertions.assertTrue(userMock.isPresent());
        Assertions.assertEquals(1,userMock.get().getTelegramId());
        Assertions.assertEquals("user1",userMock.get().getUserName());
    }

    @Test
    void userExistsTest() throws Exception{
        CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2();
        createUserRequestV2.setUserId(2);
        createUserRequestV2.setUserName("user2");
        mockMvc.perform(post("/v2/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createUserRequestV2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Пользователь уже существует"))
                .andExpect(jsonPath("$.type").value("UserAlreadyExists"))
                .andExpect(jsonPath("$.code").value("409"))
                .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void userDoubleRegisterTest() throws Exception{
        CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2();
        createUserRequestV2.setUserId(3);
        createUserRequestV2.setUserName("12345");
        mockMvc.perform(post("/v2/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createUserRequestV2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists());
        mockMvc.perform(post("/v2/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createUserRequestV2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Пользователь уже существует"))
                .andExpect(jsonPath("$.type").value("UserAlreadyExists"))
                .andExpect(jsonPath("$.code").value("409"))
                .andExpect(jsonPath("$.traceId").exists());
    }
}
