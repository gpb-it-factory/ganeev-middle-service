package ru.gpb.middle_service.dto.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;

import ru.gpb.middle_service.backendMock.entity.AccountMock;


@Data
@AllArgsConstructor
public class AccountsListResponseV2 {
    private String accountId;

    private String accountName;

    private double amount;


    public AccountsListResponseV2(AccountMock accountMock){
        accountId = accountMock.getId();
        accountName = accountMock.getAccountName();
        amount = accountMock.getAmount();
    }

}
