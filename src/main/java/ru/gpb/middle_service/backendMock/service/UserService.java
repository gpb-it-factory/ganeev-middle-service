package ru.gpb.middle_service.backendMock.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import ru.gpb.middle_service.backendMock.repository.UserRepository;
import ru.gpb.middle_service.backendMock.UtilsService;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.backendMock.exception.UserAlreadyExistsException;
import ru.gpb.middle_service.dto.users.CreateUserRequestV2;
import ru.gpb.middle_service.dto.users.UserResponseV2;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public UserResponseV2 createMockUser(CreateUserRequestV2 userRequest) {

        findByTelegramId(userRequest.getUserId()).ifPresent(userMock -> {
            throw new UserAlreadyExistsException();
        });
        findByUserName(userRequest.getUserName()).ifPresent(userMock -> {
            throw new UserAlreadyExistsException();
        });
        UserMock userMock = new UserMock(UtilsService.generateUUID(),
                userRequest.getUserId(),
                userRequest.getUserName());
        userRepository.save(userMock);
        return new UserResponseV2(userMock.getId());
    }

    public Optional<UserMock> findByTelegramId(long telegramId){
        return userRepository.findByTelegramId(telegramId);
    }

    public Optional<UserMock> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
