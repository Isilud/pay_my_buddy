package com.paymybuddy.application.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.application.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Iterable<Transaction> findByUserIdOrFriendId(int userId, int friendId);

    public default Iterable<Transaction> findTransactionWithId(Integer id) {
        return findByUserIdOrFriendId(id, id);
    };
}
