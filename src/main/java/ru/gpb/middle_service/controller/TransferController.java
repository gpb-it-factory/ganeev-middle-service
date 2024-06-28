package ru.gpb.middle_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gpb.middle_service.backendMockClient.BackendMockClient;
import ru.gpb.middle_service.dto.accounts.AccountsListResponseV2;
import ru.gpb.middle_service.dto.accounts.CreateAccountRequestV2;
import ru.gpb.middle_service.dto.accounts.CreateTransferRequestV2;

import java.net.URI;

@RestController
@RequestMapping("/v2/transfers")
@AllArgsConstructor
public class TransferController {
    private final BackendMockClient backendMockClient;

    @PostMapping()
    ResponseEntity<String> transferMoney(@RequestBody CreateTransferRequestV2 createTransferRequestV2) {
        return ResponseEntity.ok(backendMockClient.transferMoney(
                createTransferRequestV2.getUserNameFrom(),
                createTransferRequestV2.getUserNameTo(),
                createTransferRequestV2.getAmount()
                )
        );
    }
}
