package ru.gpb.middle_service.backendMockClient;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gpb.middle_service.backendMock.service.UserService;
import ru.gpb.middle_service.dto.CreateUserRequestV2;
import ru.gpb.middle_service.dto.UserResponseV2;

@Component
@AllArgsConstructor
public class BackendMockClient {
    private UserService userService;
    public UserResponseV2 createMockUser(CreateUserRequestV2 request){
        return userService.createMockUser(request);
    }
}
