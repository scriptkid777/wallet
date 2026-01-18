package com.wallet.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class WalletOperationRequest {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;

    public enum OperationType {
        DEPOSIT, WITHDRAW
    }

    @JsonCreator
    public WalletOperationRequest(
            @JsonProperty("walletId") UUID walletId,
            @JsonProperty("operationType") OperationType operationType,
            @JsonProperty("amount") BigDecimal amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }
}
