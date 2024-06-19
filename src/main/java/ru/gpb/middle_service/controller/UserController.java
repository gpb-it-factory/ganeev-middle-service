package ru.gpb.middle_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gpb.middle_service.backendMock.service.UserService;
import ru.gpb.middle_service.dto.CreateUserRequestV2;
import ru.gpb.middle_service.dto.UserResponseV2;

@RestController
@AllArgsConstructor
@RequestMapping("/v2/users")
public class UserController {
    private final UserService userService;
    @PostMapping
    UserResponseV2 createUser(@RequestBody CreateUserRequestV2 createUserRequest){
         return userService.createMockUser(createUserRequest);
    };
}
