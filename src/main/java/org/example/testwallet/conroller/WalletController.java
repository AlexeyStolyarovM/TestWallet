package org.example.testwallet.conroller;

import org.example.testwallet.model.WalletOperation;
import org.example.testwallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<String> updateWallet(@RequestBody WalletOperation operation) {
        try {
            walletService.updateBalance(operation);
            return ResponseEntity.ok("Operation successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Long> getBalance(@PathVariable UUID walletId) {
        Long balance = walletService.getBalance(walletId);
        return balance != null ? ResponseEntity.ok(balance) : ResponseEntity.notFound().build();
    }
}
