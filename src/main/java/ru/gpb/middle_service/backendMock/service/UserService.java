package ru.gpb.middle_service.backendMock.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gpb.middle_service.backendMock.UserRepository;
import ru.gpb.middle_service.backendMock.UtilsService;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.backendMock.exception.UserAlreadyExistsException;
import ru.gpb.middle_service.dto.CreateUserRequestV2;
import ru.gpb.middle_service.dto.UserResponseV2;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public UserResponseV2 createMockUser(CreateUserRequestV2 userRequest) {
        userRepository.findByTelegramId(userRequest.getUserId()).ifPresent(userMock -> {
            throw new UserAlreadyExistsException();
        });
        UserMock userMock = new UserMock(UtilsService.generateUUID(),
                userRequest.getUserId(),
                userRequest.getUserName());
        userRepository.save(userMock);
        return new UserResponseV2(userMock.getId());
    }
}
