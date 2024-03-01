package com.paymybuddy.application.unit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.TransactionRepository;
import com.paymybuddy.application.service.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    Transaction defaultTransaction;
    User defaultUser;

    @BeforeEach
    public void clear() {
        transactionService = new TransactionService(transactionRepository);
        defaultTransaction = Transaction.builder().senderEmail("originEmail").senderName("originName")
                .recipientEmail("destinationEmail").recipientName("destinationName").amount(100)
                .date("2024-01-01").description("Description").build();
        defaultUser = User.builder().email("originEmail").build();
    }

    @Test
    public void getTransactionWithEmail() {
        when(transactionRepository.findBySenderEmailOrRecipientEmail("originEmail")).thenReturn(null);
        transactionService.getTransactionsByEmail(defaultUser);

        verify(transactionRepository).findBySenderEmailOrRecipientEmail("originEmail");
    }

    @Test
    public void createTransaction() {
        Transaction defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(transactionRepository.save(defaultTransactionWithInterest)).thenReturn(
                defaultTransactionWithInterest);
        transactionService.createTransaction(defaultTransaction);

        verify(transactionRepository).save(defaultTransactionWithInterest);
    }

}
