package com.wallet.service;

import com.wallet.controller.WalletController;
import com.wallet.dto.WalletOperationRequest;
import com.wallet.entity.Wallet;
import com.wallet.exception.InsufficientFundsException;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;


@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void processOperation(WalletOperationRequest request) {
        Wallet wallet = walletRepository.findByIdForUpdate(request.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException(request.getWalletId()));


        BigDecimal newBalance;

        switch (request.getOperationType()) {
            case DEPOSIT:
                newBalance = wallet.getBalance().add(request.getAmount());
                break;
            case WITHDRAW:
                if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                    throw new InsufficientFundsException(request.getWalletId());
                }
                newBalance = wallet.getBalance().subtract(request.getAmount());
                break;
            default:
                throw new IllegalArgumentException("Invalid operation type");
        }

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }

        @Transactional(readOnly = true)
        public BigDecimal getBalance(UUID walletId) {
            Wallet wallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new WalletNotFoundException(walletId));
            return wallet.getBalance();
        }

    @Transactional
    public Wallet createWallet(UUID walletId) {
        Wallet wallet = new Wallet(walletId, BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }
}
