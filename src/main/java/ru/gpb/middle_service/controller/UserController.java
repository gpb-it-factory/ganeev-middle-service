package ru.gpb.middle_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.gpb.middle_service.backendMockClient.BackendMockClient;
import ru.gpb.middle_service.dto.accounts.AccountsListResponseV2;
import ru.gpb.middle_service.dto.accounts.CreateAccountRequestV2;
import ru.gpb.middle_service.dto.users.CreateUserRequestV2;
import ru.gpb.middle_service.dto.users.UserResponseV2;

import java.net.URI;


@RestController
@AllArgsConstructor
@RequestMapping("/v2/users")
public class UserController {
    private final BackendMockClient backendMockClient;
    @PostMapping
    ResponseEntity<UserResponseV2> createUser(@RequestBody CreateUserRequestV2 createUserRequest){
         return ResponseEntity.created(URI.create("/v2/users")).body(backendMockClient.createMockUser(createUserRequest));

    };

    @PostMapping("/{id}/accounts")
    ResponseEntity<AccountsListResponseV2> createAccount(@PathVariable("id") long id, @RequestBody(required = false) CreateAccountRequestV2 createAccountRequestV2){
        return ResponseEntity.created(URI.create("/v2/users/"+id+"/accounts")).body(backendMockClient.createUserAccount(id,createAccountRequestV2));
    }


    @GetMapping("/{id}/accounts")
    ResponseEntity<AccountsListResponseV2> getAccount(@PathVariable("id") long id){
        return ResponseEntity.ok(backendMockClient.getUserAccount(id));
    }


}
