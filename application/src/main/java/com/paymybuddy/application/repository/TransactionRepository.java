package com.paymybuddy.application.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.application.model.Transaction;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // @Query("SELECT t FROM Transaction t WHERE t.sender_email = :email OR
    // t.recipient_email = :email")
    @Query("SELECT u FROM User u WHERE u.status = ?1 and u.name = ?2")
    List<Transaction> findBySenderEmailOrRecipientEmail(String email);

}
