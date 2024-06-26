package ru.gpb.middle_service.backendMock.repository;

import org.springframework.stereotype.Repository;
import ru.gpb.middle_service.backendMock.entity.AccountMock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AccountRepository {
    private final Map<String, AccountMock> accounts;

    public AccountRepository() {
        this.accounts = new HashMap<>();
    }
    public void save(String userId,AccountMock accountMock){
        accounts.put(userId,accountMock);
    }
    public Optional<AccountMock> findByUserId(String userId){
      return Optional.ofNullable(accounts.get(userId));
    };
    public void clear(){
        accounts.clear();
    }


}
