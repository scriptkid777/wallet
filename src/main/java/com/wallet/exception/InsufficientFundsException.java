package com.wallet.exception;

import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID walletId) {
        super("Insufficient funds in wallet with id '" + walletId + "'");
    }
    public InsufficientFundsException(String message) {
        super(message);
    }
}
