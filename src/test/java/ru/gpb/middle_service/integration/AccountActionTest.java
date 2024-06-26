package ru.gpb.middle_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gpb.middle_service.backendMock.entity.AccountMock;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.backendMock.repository.AccountRepository;
import ru.gpb.middle_service.backendMock.repository.UserRepository;
import ru.gpb.middle_service.dto.accounts.CreateAccountRequestV2;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountActionTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        accountRepository.clear();
        userRepository.clear();
        accountRepository.save("111",new AccountMock("111",5000,"account1"));
        userRepository.save(new UserMock("111",1,"user1"));
        userRepository.save(new UserMock("222",2,"user2"));
    }

    @Test
    void successCreateAccountTest() throws Exception {
        CreateAccountRequestV2 createAccountRequestV2 = new CreateAccountRequestV2();
        createAccountRequestV2.setAccountName("12345");
        String json = mapper.writeValueAsString(createAccountRequestV2);
        mockMvc.perform(post("/v2/users/2/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").exists())
                .andExpect(jsonPath("$.accountName").value("12345"))
                .andExpect(jsonPath("$.amount").value(5000d));
        Optional<AccountMock> accountMock = accountRepository.findByUserId("222");
        Assertions.assertTrue(accountMock.isPresent());
        AccountMock account = accountMock.get();
        Assertions.assertEquals("12345",account.getAccountName());
        Assertions.assertEquals(5000d,account.getAmount());
        Assertions.assertNotNull(account.getId());
    }
    @Test
    void userNotExistsTest() throws Exception{
        CreateAccountRequestV2 createUserRequestV2 = new CreateAccountRequestV2();
        mockMvc.perform(post("/v2/users/3/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createUserRequestV2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь не найден"))
                .andExpect(jsonPath("$.type").value("UserNotFound"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.traceId").exists());
    }
    @Test
    void accountExistTest() throws Exception{
        CreateAccountRequestV2 createUserRequestV2 = new CreateAccountRequestV2();
        mockMvc.perform(post("/v2/users/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createUserRequestV2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Аккаунт уже существует"))
                .andExpect(jsonPath("$.type").value("AccountAlreadyExist"))
                .andExpect(jsonPath("$.code").value("409"))
                .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void getExistAccountTest() throws Exception {
        mockMvc.perform(get("/v2/users/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").exists())
                .andExpect(jsonPath("$.accountName").value("account1"))
                .andExpect(jsonPath("$.amount").value(5000d));
    }
    @Test
    void getNotExistAccountTest() throws Exception {
        mockMvc.perform(get("/v2/users/2/accounts"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Счет не найден"))
                .andExpect(jsonPath("$.type").value("AccountNotFound"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.traceId").exists());
    }
    @Test
    void getAccountForNotExistUserTest() throws Exception {
        mockMvc.perform(get("/v2/users/3/accounts"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь не найден"))
                .andExpect(jsonPath("$.type").value("UserNotFound"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.traceId").exists());
    }
}
