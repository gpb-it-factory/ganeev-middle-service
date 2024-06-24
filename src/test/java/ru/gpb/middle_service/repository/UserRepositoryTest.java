package ru.gpb.middle_service.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.gpb.middle_service.backendMock.repository.UserRepository;
import ru.gpb.middle_service.backendMock.entity.UserMock;

import java.util.Optional;

public class UserRepositoryTest {
    private UserRepository userRepository;
    private UserMock userMock;

    @BeforeEach
    void setUp(){
        userRepository = new UserRepository();
        userMock = new UserMock("111-222-333",1,"12345");

    }
    @Test
    void saveAndFindByTelegramIdTest(){
        userRepository.save(userMock);
        Optional<UserMock> optionalUserMock = userRepository.findByTelegramId(1);
        Assertions.assertTrue(optionalUserMock.isPresent());
        Assertions.assertEquals(1,optionalUserMock.get().getTelegramId());
        Assertions.assertEquals("111-222-333",optionalUserMock.get().getId());
        Assertions.assertEquals("12345",optionalUserMock.get().getUserName());
    }

    @Test
    void failedSaveAndFindByTelegramIdTest(){
        userRepository.save(userMock);
        Optional<UserMock> optionalUserMock = userRepository.findByTelegramId(2);
        Assertions.assertTrue(optionalUserMock.isEmpty());
    }
    @Test
    void saveAndFindByUserNameTest(){
        userRepository.save(userMock);
        Optional<UserMock> optionalUserMock = userRepository.findByUserName("12345");
        Assertions.assertTrue(optionalUserMock.isPresent());
        Assertions.assertEquals(1,optionalUserMock.get().getTelegramId());
        Assertions.assertEquals("111-222-333",optionalUserMock.get().getId());
        Assertions.assertEquals("12345",optionalUserMock.get().getUserName());
    }

    @Test
    void failedSaveAndFindByTelegramUserNameTest(){
        userRepository.save(userMock);
        Optional<UserMock> optionalUserMock = userRepository.findByUserName("1234");
        Assertions.assertTrue(optionalUserMock.isEmpty());
    }

}
