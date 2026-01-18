package com.wallet.controller;

import com.wallet.dto.WalletOperationRequest;
import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
    @Autowired
    private WalletService walletService;



    @PostMapping("/wallet")
    public ResponseEntity<Void> processOperation(@RequestBody WalletOperationRequest request) {
        walletService.processOperation(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/wallets/{walletId}")
    public ResponseEntity<Void> createWallet(@PathVariable UUID walletId) {
        walletService.createWallet(walletId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
