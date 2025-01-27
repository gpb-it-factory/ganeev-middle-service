package ru.gpb.middle_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import ru.gpb.middle_service.backendMock.UtilsService;
import ru.gpb.middle_service.backendMock.entity.AccountMock;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.backendMock.exception.AccountAlreadyExistException;
import ru.gpb.middle_service.backendMock.exception.UserNotFoundException;
import ru.gpb.middle_service.backendMock.repository.AccountRepository;
import ru.gpb.middle_service.backendMock.service.AccountService;
import ru.gpb.middle_service.backendMock.service.UserService;
import ru.gpb.middle_service.dto.accounts.AccountsListResponseV2;
import ru.gpb.middle_service.dto.accounts.CreateAccountRequestV2;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserService userService;

    private MockedStatic<UtilsService> utilsServiceMockedStatic;

    @BeforeEach
    void setUp(){
        utilsServiceMockedStatic = Mockito.mockStatic(UtilsService.class);
        utilsServiceMockedStatic.when(UtilsService::generateUUID).thenReturn("111-222-333");
    }
    @AfterEach
    void tearDown(){
        utilsServiceMockedStatic.close();
    }
    @Test
    void createAccountForNotExistUserTest(){
        CreateAccountRequestV2 request = new CreateAccountRequestV2();
        Mockito.when(userService.findByTelegramId(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class,()->{
            accountService.createUserAccount(1,request);
        });
    }
    @Test
    void createAccountForUserWithAccountTest(){
        CreateAccountRequestV2 request = new CreateAccountRequestV2();
        Mockito.when(userService.findByTelegramId(Mockito.anyLong())).thenReturn(Optional.of(new UserMock("111",1,"user1")));
        Mockito.when(accountRepository.findByUserId(Mockito.anyString())).thenReturn(Optional.of(new AccountMock("111",50000,"12345")));
        Assertions.assertThrows(AccountAlreadyExistException.class,()->{
            accountService.createUserAccount(1,request);
        });
    }
    @Test
    void successCreateAccountForUserTest(){
        CreateAccountRequestV2 request = new CreateAccountRequestV2();
        request.setAccountName("account1");
        Mockito.when(userService.findByTelegramId(Mockito.anyLong())).thenReturn(Optional.of(new UserMock("111",1,"user1")));
        AccountsListResponseV2 response = accountService.createUserAccount(1,request);
        Assertions.assertEquals("account1",response.getAccountName());
        Assertions.assertNotNull(response.getAccountId());
        Assertions.assertEquals(5000d,response.getAmount());
    }

    @Test
    void successCreateAccountWithoutNameForUserTest(){
        CreateAccountRequestV2 request = new CreateAccountRequestV2();
        Mockito.when(userService.findByTelegramId(Mockito.anyLong())).thenReturn(Optional.of(new UserMock("111",1,"user1")));
        AccountsListResponseV2 response = accountService.createUserAccount(1,request);
        Assertions.assertEquals(AccountService.DEFAULT_NAME,response.getAccountName());
        Assertions.assertNotNull(response.getAccountId());
        Assertions.assertEquals(5000d,response.getAmount());
    }
}
