package org.example.testwallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor
@Getter@Setter
public class WalletOperation {

    @JsonProperty("walletId")
    private UUID walletId;

    @JsonProperty("operationType")
    private OperationType operationType;

    @JsonProperty("amount")
    private long amount;
}
