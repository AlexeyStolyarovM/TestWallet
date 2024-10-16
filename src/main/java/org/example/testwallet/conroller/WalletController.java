package org.example.testwallet.conroller;

import org.example.testwallet.model.WalletOperation;
import org.example.testwallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<String> updateWallet(@RequestBody WalletOperation operation) {
        logger.info("Updating wallet with ID: {}", operation.getWalletId());
        try {
            walletService.updateBalance(operation);
            logger.info("Operation successful for wallet ID: {}", operation.getWalletId());
            return ResponseEntity.ok("Operation successful");
        } catch (Exception e) {
            logger.error("Error updating wallet with ID: {}", operation.getWalletId(), e);
            throw e;  // Или выбросьте кастомное исключение
        }
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Long> getBalance(@PathVariable UUID walletId) {
        logger.info("Retrieving balance for wallet ID: {}", walletId);
        Long balance = walletService.getBalance(walletId);
        return balance != null ? ResponseEntity.ok(balance) : ResponseEntity.notFound().build();
    }
}
