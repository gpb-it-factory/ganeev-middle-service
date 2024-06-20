package ru.gpb.middle_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.gpb.middle_service.backendMock.exception.UserAlreadyExistsException;
import ru.gpb.middle_service.backendMockClient.BackendMockClient;
import ru.gpb.middle_service.controller.advice.UserControllerAdvice;
import ru.gpb.middle_service.dto.CreateUserRequestV2;
import ru.gpb.middle_service.dto.UserResponseV2;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mvc;
    @Mock
    private BackendMockClient backendMockClient;
    @InjectMocks
    private UserController userController;

    private UserControllerAdvice userControllerAdvice;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        userControllerAdvice = new UserControllerAdvice();
        mvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(userControllerAdvice).build();
        mapper = new ObjectMapper();
        Mockito.when(backendMockClient.createMockUser(Mockito.any(CreateUserRequestV2.class))).thenAnswer(
                invocation -> {
                    CreateUserRequestV2 createUserRequestV2 = invocation.getArgument(0);
                    if (createUserRequestV2.getUserId()==1){
                        return new UserResponseV2("12345");
                    }  else {
                        throw  new UserAlreadyExistsException();
                    }
                }
        );
    }

    @Test
    void SuccessCreateUserTest() throws Exception {
        CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2();
        createUserRequestV2.setUserId(1);
        String json = mapper.writeValueAsString(createUserRequestV2);
        mvc.perform(post("/v2/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.userId").value("12345"));
        verify(backendMockClient,times(1)).createMockUser(Mockito.any(CreateUserRequestV2.class));
    }

    @Test
    void FailedCreateUserTest() throws Exception {
        CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2();
        createUserRequestV2.setUserId(2);
        String json = mapper.writeValueAsString(createUserRequestV2);
        mvc.perform(post("/v2/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Пользователь уже существует"))
                .andExpect(jsonPath("$.type").value("UserAlreadyExists"))
                .andExpect(jsonPath("$.code").value("409"))
                .andExpect(jsonPath("$.traceId").exists())
        ;
        verify(backendMockClient,times(1)).createMockUser(Mockito.any(CreateUserRequestV2.class));
    }
}