package ru.gpb.middle_service.service;

import org.apache.catalina.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gpb.middle_service.backendMock.UserRepository;
import ru.gpb.middle_service.backendMock.UtilsService;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.backendMock.exception.UserAlreadyExistsException;
import ru.gpb.middle_service.backendMock.service.UserService;
import ru.gpb.middle_service.dto.CreateUserRequestV2;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private  UserService userService;

    @Mock
    private UserRepository userRepository;
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
    void failedCreateMockUser(){
        CreateUserRequestV2 createUserRequestV2  = new CreateUserRequestV2();
        createUserRequestV2.setUserId(1);
        Mockito.when(userRepository.findByTelegramId(1))
                .thenReturn(Optional.of(new UserMock(
                        "111-222-333",
                        1,
                        "user")));
        Assertions.assertThrows(UserAlreadyExistsException.class,()->{
            userService.createMockUser(createUserRequestV2);
        });
    }
    @Test
    void successCreateMockUser(){
        CreateUserRequestV2 createUserRequestV2  = new CreateUserRequestV2();
        createUserRequestV2.setUserId(1);
        Mockito.when(userRepository.findByTelegramId(1))
                .thenReturn(Optional.ofNullable(null));
        Assertions.assertEquals("111-222-333",userService.createMockUser(createUserRequestV2).getUserId());
    }

}
