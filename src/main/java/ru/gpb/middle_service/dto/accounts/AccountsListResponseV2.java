package ru.gpb.middle_service.dto.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountsListResponseV2 {
    private String accountId;

    private String accountName;

    private double amount;
}
