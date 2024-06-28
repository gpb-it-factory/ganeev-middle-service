package ru.gpb.middle_service.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gpb.middle_service.backendMock.UtilsService;

import ru.gpb.middle_service.backendMock.exception.*;

import ru.gpb.middle_service.dto.ErrorV2;

@RestControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ErrorV2> userAlreadyExistsExceptionHandler(){
          return ResponseEntity.status(409).body(ErrorV2.builder()
                          .message("Пользователь уже существует")
                          .type("UserAlreadyExists")
                          .code("409")
                          .traceId(UtilsService.generateUUID())
                          .build());
    };

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorV2> userNotFoundExceptionHandler(){
        return ResponseEntity.status(404).body(ErrorV2.builder()
                .message("Пользователь не найден")
                .type("UserNotFound")
                .code("404")
                .traceId(UtilsService.generateUUID())
                .build());
    };
    @ExceptionHandler(AccountAlreadyExistException.class)
    ResponseEntity<ErrorV2> AccountAlreadyExistsExceptionHandler(){
        return ResponseEntity.status(409).body(ErrorV2.builder()
                .message("Аккаунт уже существует")
                .type("AccountAlreadyExist")
                .code("409")
                .traceId(UtilsService.generateUUID())
                .build());
    };

    @ExceptionHandler(AccountNotFoundException.class)
    ResponseEntity<ErrorV2> accountNotFoundExceptionHandler(){
        return ResponseEntity.status(404).body(ErrorV2.builder()
                .message("Счет не найден")
                .type("AccountNotFound")
                .code("404")
                .traceId(UtilsService.generateUUID())
                .build());
    };

    @ExceptionHandler(LowBalanceException.class)
    ResponseEntity<ErrorV2> lowBalanceExceptionHandler(){
        return ResponseEntity.status(409).body(ErrorV2.builder()
                .message("Недостаточно средств на счете")
                .type("LowBalance")
                .code("409")
                .traceId(UtilsService.generateUUID())
                .build());
    };

}
