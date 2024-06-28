package ru.gpb.middle_service.service;


import org.apache.catalina.User;

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

import ru.gpb.middle_service.backendMock.exception.AccountNotFoundException;

import ru.gpb.middle_service.backendMock.exception.LowBalanceException;
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

    @Test
    void successGetUserAccountTest(){
        Mockito.when(userService.findByTelegramId(Mockito.anyLong()))
                .thenReturn(Optional.of(new UserMock("111",1,"user1")));
        Mockito.when(accountRepository.findByUserId(Mockito.anyString())).thenReturn(Optional.of(new AccountMock("111",5000d,"account1")));
        AccountsListResponseV2 result = accountService.getUserAccount(1);
        Assertions.assertEquals("111",result.getAccountId());
        Assertions.assertEquals(5000d,result.getAmount());
        Assertions.assertEquals("account1",result.getAccountName());
    }

    @Test
    void failedGetNotExistUserAccountTest(){
        Mockito.when(userService.findByTelegramId(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,()->accountService.getUserAccount(1));
    }

    @Test
    void failedGetUserNotExistAccountTest(){
        Mockito.when(userService.findByTelegramId(Mockito.anyLong()))
                .thenReturn(Optional.of(new UserMock("111",1,"user1")));
        Mockito.when(accountRepository.findByUserId(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(AccountNotFoundException.class,()->accountService.getUserAccount(1));
    }

    @Test
    void failedTransferForNotExistUser(){
        Mockito.when(userService.findByUserName(Mockito.anyString())).thenThrow(new UserNotFoundException());
        Assertions.assertThrows(UserNotFoundException.class,()->accountService.transferMoney("1","2",500));
    }
    @Test
    void failedTransferForNotExistAccount(){
        Mockito.when(userService.findByUserName("111")).thenReturn(Optional.of(new UserMock("111", 1, "user1")));
        Mockito.when(accountRepository.findByUserId("111")).thenThrow(new AccountNotFoundException());
        Assertions.assertThrows(AccountNotFoundException.class,()->accountService.transferMoney("111","222",500));
    }

    @Test
    void failedTransferForLowBalanceAccount(){
        Mockito.when(userService.findByUserName("111")).thenReturn(Optional.of(new UserMock("111", 1, "user1")));
        Mockito.when(accountRepository.findByUserId("111")).thenReturn(Optional.of(new AccountMock("123", 499, "account1")));
        Assertions.assertThrows(LowBalanceException.class,()->accountService.transferMoney("111","222",500));
    }
    @Test
    void successTransferForLowBalanceAccount(){
        Mockito.when(userService.findByUserName("111")).thenReturn(Optional.of(new UserMock("111", 1, "user1")));
        Mockito.when(userService.findByUserName("222")).thenReturn(Optional.of(new UserMock("222", 2, "user2")));
        Mockito.when(accountRepository.findByUserId("111")).thenReturn(Optional.of(new AccountMock("111", 1000, "account1")));
        Mockito.when(accountRepository.findByUserId("222")).thenReturn(Optional.of(new AccountMock("222", 500, "account2")));
        Assertions.assertEquals("success",accountService.transferMoney("111","222",500));
    }


}
