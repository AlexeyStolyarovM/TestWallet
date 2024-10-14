package org.example.testwallet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@RequiredArgsConstructor
@Getter@Setter
@Entity
public class Wallet {
    @Id
    private UUID walletId;
    private long balance;
}
