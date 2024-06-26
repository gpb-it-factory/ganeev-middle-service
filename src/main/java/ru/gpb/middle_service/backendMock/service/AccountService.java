package ru.gpb.middle_service.backendMock.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gpb.middle_service.backendMock.UtilsService;
import ru.gpb.middle_service.backendMock.entity.AccountMock;
import ru.gpb.middle_service.backendMock.entity.UserMock;
import ru.gpb.middle_service.backendMock.exception.AccountAlreadyExistException;

import ru.gpb.middle_service.backendMock.exception.AccountNotFoundException;

import ru.gpb.middle_service.backendMock.exception.UserNotFoundException;
import ru.gpb.middle_service.backendMock.repository.AccountRepository;
import ru.gpb.middle_service.dto.accounts.AccountsListResponseV2;
import ru.gpb.middle_service.dto.accounts.CreateAccountRequestV2;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private UserService userService;
    private AccountRepository accountRepository;
    private static final double BONUS_AMOUNT = 5000;
    public static final String DEFAULT_NAME = "Акционный";
    public AccountsListResponseV2 createUserAccount(long userId, CreateAccountRequestV2 request){
        UserMock user = userService.findByTelegramId(userId).orElseThrow(UserNotFoundException::new);
        String userUUID = user.getId();
        accountRepository.findByUserId(userUUID).ifPresent(accountMock -> {
            throw new AccountAlreadyExistException();
        });
        String accountName = request==null || request.getAccountName()==null || request.getAccountName().isEmpty()?DEFAULT_NAME: request.getAccountName();
        AccountMock account = new AccountMock(UtilsService.generateUUID(),BONUS_AMOUNT, accountName);
        accountRepository.save(userUUID,account);
        return new AccountsListResponseV2(account);
    }

    public AccountsListResponseV2 getUserAccount(long userId){
        UserMock user = userService.findByTelegramId(userId).orElseThrow(UserNotFoundException::new);
        String userUUID = user.getId();
        AccountMock account = accountRepository.findByUserId(userUUID).orElseThrow(AccountNotFoundException::new);
        return new AccountsListResponseV2(account);
    }
}
