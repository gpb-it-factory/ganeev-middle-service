package ru.gpb.middle_service.backendMock.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AccountMock {
    private String id;

    public AccountMock(String id, double amount, String accountName) {
        this.id = id;
        setAmount(amount);
        this.accountName = accountName;
    }

    private Long amount;

    private String accountName;

    public double getAmount() {
        return  (double) (amount /100);
    }

    public void setAmount(double amount) {
        this.amount = Math.round(amount*100);
    }
}
