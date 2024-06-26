package ru.gpb.middle_service.backendMockClient;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import ru.gpb.middle_service.backendMock.service.AccountService;
import ru.gpb.middle_service.backendMock.service.UserService;
import ru.gpb.middle_service.dto.accounts.AccountsListResponseV2;
import ru.gpb.middle_service.dto.accounts.CreateAccountRequestV2;
import ru.gpb.middle_service.dto.users.CreateUserRequestV2;
import ru.gpb.middle_service.dto.users.UserResponseV2;


@Component
@AllArgsConstructor
public class BackendMockClient {
    private UserService userService;

    private AccountService accountService;

    public UserResponseV2 createMockUser(CreateUserRequestV2 request){
        return userService.createMockUser(request);
    }
    public AccountsListResponseV2 createUserAccount(long userId, CreateAccountRequestV2 request){
        return accountService.createUserAccount(userId,request);
    }


    public AccountsListResponseV2 getUserAccount(long userId){
        return accountService.getUserAccount(userId);
    }

}
