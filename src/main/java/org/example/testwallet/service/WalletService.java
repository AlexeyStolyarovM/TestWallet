package org.example.testwallet.service;

import org.example.testwallet.model.OperationType;
import org.example.testwallet.model.Wallet;
import org.example.testwallet.model.WalletOperation;
import org.example.testwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void updateBalance(WalletOperation operation) {
        Wallet wallet = walletRepository.findById(operation.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (operation.getOperationType() == OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance() + operation.getAmount());
        } else if (operation.getOperationType() == OperationType.WITHDRAW) {
            if (wallet.getBalance() < operation.getAmount()) {
                throw new RuntimeException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance() - operation.getAmount());
        }

        walletRepository.save(wallet);
    }

    public Long getBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElse(null);
    }
}
