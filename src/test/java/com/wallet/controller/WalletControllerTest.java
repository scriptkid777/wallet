package com.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.WalletOperationRequest;
import com.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(WalletController.class)
class WalletControllerTest {
    private UUID testWalletId;
    private BigDecimal testAmount;

    @BeforeEach
    void setUp() {
        testWalletId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        testAmount = new BigDecimal("1000.00");
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProcessDeposit() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletOperationRequest request = new WalletOperationRequest(
                walletId,
                WalletOperationRequest.OperationType.DEPOSIT,
                new BigDecimal("1000.00")
        );

        doNothing().when(walletService).processOperation(any());

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testProcessWithdrawSucces() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(
                testWalletId,
                WalletOperationRequest.OperationType.WITHDRAW,
                new BigDecimal("500.00")
        );
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        verify(walletService, times(1)).processOperation(any(WalletOperationRequest.class));
    }

    @Test
    void testGetBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = new BigDecimal("5000.00");

        when(walletService.getBalance(walletId)).thenReturn(balance);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().string("5000.00"));
    }

    @Test
    void testInvalidJson() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void processOperation_InvalidOperationType() throws Exception {
        // JSON с несуществующим типом операции
        String invalidJson = """
            {
                "walletId": "123e4567-e89b-12d3-a456-426614174000",
                "operationType": "INVALID_TYPE",
                "amount": 1000.00
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void wrongHttpMethod() throws Exception {
        // Пробуем отправить GET вместо POST
        mockMvc.perform(get("/api/v1/wallet"))
                .andExpect(status().isMethodNotAllowed());  // HTTP 405
    }

    @Test
    void nonExistentEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/nonexistent"))
                .andExpect(status().isNotFound());  // HTTP 404
    }
}