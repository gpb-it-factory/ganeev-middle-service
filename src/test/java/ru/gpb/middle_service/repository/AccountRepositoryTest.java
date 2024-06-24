package ru.gpb.middle_service.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gpb.middle_service.backendMock.entity.AccountMock;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.backendMock.repository.AccountRepository;
import ru.gpb.middle_service.backendMock.repository.UserRepository;

import java.util.Optional;

public class AccountRepositoryTest {
    private AccountRepository accountRepository;


    @BeforeEach
    void setUp(){
        accountRepository = new AccountRepository();
        accountRepository.save("111",new AccountMock("222",5000,"12345"));
    }
    @Test
    void successFindByUserIdTest(){
        Optional<AccountMock> accountMock = accountRepository.findByUserId("111");
        Assertions.assertTrue(accountMock.isPresent());
        AccountMock result = accountMock.get();
        Assertions.assertEquals("222",result.getId());
        Assertions.assertEquals(5000d,result.getAmount());
        Assertions.assertEquals("12345",result.getAccountName());
    }
    @Test
    void failedFindByUserIdTest(){
        Optional<AccountMock> accountMock = accountRepository.findByUserId("222");
        Assertions.assertTrue(accountMock.isEmpty());
    }
}
