package ru.gpb.middle_service.dto.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransferRequestV2 {
    private String userNameFrom;
    private String userNameTo;
    private double amount;
}
