package ru.gpb.middle_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.gpb.middle_service.backendMock.exception.AccountNotFoundException;
import ru.gpb.middle_service.backendMock.exception.LowBalanceException;
import ru.gpb.middle_service.backendMock.exception.UserNotFoundException;
import ru.gpb.middle_service.backendMockClient.BackendMockClient;
import ru.gpb.middle_service.controller.advice.UserControllerAdvice;
import ru.gpb.middle_service.dto.accounts.CreateTransferRequestV2;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    private MockMvc mvc;
    @Mock
    private BackendMockClient backendMockClient;
    @InjectMocks
    private TransferController transferController;

    private UserControllerAdvice userControllerAdvice;
    private ObjectMapper mapper;
    @BeforeEach
    void setUp(){
        userControllerAdvice = new UserControllerAdvice();
        mvc = MockMvcBuilders.standaloneSetup(transferController).setControllerAdvice(userControllerAdvice).build();
        mapper = new ObjectMapper();
    }
    @Test
    void failedOneOfUsersNotFoundExceptionTest() throws Exception {
        CreateTransferRequestV2 createTransferRequestV2 = new CreateTransferRequestV2("user1","user2",500);
        Mockito.when(backendMockClient.transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble()))
                .thenThrow(new UserNotFoundException());
        mvc.perform(post("/v2/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createTransferRequestV2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь не найден"))
                .andExpect(jsonPath("$.type").value("UserNotFound"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.traceId").exists());
        verify(backendMockClient,times(1)).transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble());
    }
    @Test
    void failedAccountNotFoundExceptionTest() throws Exception {
        CreateTransferRequestV2 createTransferRequestV2 = new CreateTransferRequestV2("user1","user2",500);
        Mockito.when(backendMockClient.transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble()))
                .thenThrow(new AccountNotFoundException());
        mvc.perform(post("/v2/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createTransferRequestV2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Счет не найден"))
                .andExpect(jsonPath("$.type").value("AccountNotFound"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.traceId").exists());
        verify(backendMockClient,times(1)).transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble());
    }

    @Test
    void failedLowBalanceExceptionTest() throws Exception {
        CreateTransferRequestV2 createTransferRequestV2 = new CreateTransferRequestV2("user1","user2",500);
        Mockito.when(backendMockClient.transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble()))
                .thenThrow(new LowBalanceException());
        mvc.perform(post("/v2/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createTransferRequestV2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Недостаточно средств на счете"))
                .andExpect(jsonPath("$.type").value("LowBalance"))
                .andExpect(jsonPath("$.code").value("409"))
                .andExpect(jsonPath("$.traceId").exists());
        verify(backendMockClient,times(1)).transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble());
    }

    @Test
    void successTransferTest() throws Exception {
        CreateTransferRequestV2 createTransferRequestV2 = new CreateTransferRequestV2("user1","user2",500);
        Mockito.when(backendMockClient.transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble()))
                        .thenReturn("success");
        mvc.perform(post("/v2/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createTransferRequestV2)))
                .andExpect(status().isOk())
                        .andExpect(content().string("success"));
        verify(backendMockClient,times(1)).transferMoney(Mockito.anyString(),Mockito.anyString(),Mockito.anyDouble());
    }
}
