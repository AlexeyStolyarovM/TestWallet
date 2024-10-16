package org.example.testwallet.service;

import org.example.testwallet.exception.InsufficientFundsException;
import org.example.testwallet.exception.WalletNotFoundException;
import org.example.testwallet.model.OperationType;
import org.example.testwallet.model.Wallet;
import org.example.testwallet.model.WalletOperation;
import org.example.testwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void updateBalance(WalletOperation operation) {
        logger.info("Updating balance for wallet ID: {}", operation.getWalletId());

        Wallet wallet = walletRepository.findById(operation.getWalletId())
                .orElseThrow(() -> {
                    logger.error("Wallet not found for ID: {}", operation.getWalletId());
                    return new WalletNotFoundException("Wallet not found");
                });

        if (operation.getOperationType() == OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance() + operation.getAmount());
            logger.info("Deposited {} to wallet ID: {}", operation.getAmount(), operation.getWalletId());
        } else if (operation.getOperationType() == OperationType.WITHDRAW) {
            if (wallet.getBalance() < operation.getAmount()) {
                logger.error("Insufficient funds for wallet ID: {}. Current balance: {}, Attempted withdrawal: {}",
                        operation.getWalletId(), wallet.getBalance(), operation.getAmount());
                throw new InsufficientFundsException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance() - operation.getAmount());
            logger.info("Withdrew {} from wallet ID: {}", operation.getAmount(), operation.getWalletId());
        }

        walletRepository.save(wallet);
        logger.info("Updated balance for wallet ID: {} to {}", operation.getWalletId(), wallet.getBalance());
    }

    public Long getBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(wallet -> {
                    logger.info("Retrieved balance for wallet ID: {}", walletId);
                    return wallet.getBalance();
                })
                .orElse(null);
    }
}
