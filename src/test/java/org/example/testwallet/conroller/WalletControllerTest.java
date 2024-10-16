package org.example.testwallet.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.testwallet.exception.InsufficientFundsException;
import org.example.testwallet.model.OperationType;
import org.example.testwallet.model.WalletOperation;
import org.example.testwallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testUpdateWallet_Success() throws Exception {
        WalletOperation operation = new WalletOperation();
        operation.setWalletId(UUID.randomUUID());
        operation.setOperationType(OperationType.DEPOSIT);
        operation.setAmount(100L);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isOk())
                .andExpect(content().string("Operation successful"));

    }
    @Test
    public void testUpdateWallet_Failure() throws Exception {
        WalletOperation operation = new WalletOperation();
        operation.setWalletId(UUID.randomUUID());
        operation.setOperationType(OperationType.WITHDRAW);
        operation.setAmount(50L);

        doThrow(new InsufficientFundsException("Insufficient funds")).when(walletService).updateBalance(any());

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds"));
    }

    @Test
    public void testGetBalance_ExistingWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        long expectedBalance = 1000L;

        when(walletService.getBalance(walletId)).thenReturn(expectedBalance);

        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedBalance)));
    }

    @Test
    public void testGetBalance_NonExistingWallet() throws Exception {
        UUID walletId = UUID.randomUUID();

        when(walletService.getBalance(walletId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isNotFound());
    }
}