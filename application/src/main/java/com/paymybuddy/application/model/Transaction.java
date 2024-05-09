package com.paymybuddy.application.model;

import com.paymybuddy.application.service.DateService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

        public enum Operation {
                WITHDRAW, DEPOSIT;
        }

        @Id
        @Column(name = "id", unique = true)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "user_id", nullable = true)
        private int userId;

        @Column(name = "user_name")
        private String userName;

        @Column(name = "friend_id", nullable = true)
        private int friendId;

        @Column(name = "friend_name")
        private String friendName;

        @Column(name = "amount")
        private double amount;

        @Column(name = "description")
        private String description;

        @Column(name = "date")
        private String date;

        @Column(name = "interest")
        private Double interest;

        @Column(name = "with_bank")
        private boolean withBank;

        @Enumerated(EnumType.STRING)
        @Column(name = "operation")
        private Operation operation;

        public Transaction(User user, User friend, Transaction transaction) {
                DateService dateService = new DateService();
                this.amount = (transaction.getAmount());
                this.date = dateService.currentDate();
                this.description = transaction.getDescription();
                this.friendId = (friend.getId());
                this.friendName = (friend.getName());
                this.userId = (user.getId());
                this.userName = (user.getName());
                this.withBank = (false);
                this.operation = (Operation.DEPOSIT);
                this.interest = (Math.ceil(transaction.getAmount() * 0.5) / 100.0);
        }

        public Transaction(User user, Transaction transaction) {
                DateService dateService = new DateService();
                this.amount = transaction.getAmount();
                this.date = dateService.currentDate();
                this.description = transaction.getDescription();
                this.userId = user.getId();
                this.userName = user.getName();
                this.friendName = "My Bank";
                this.withBank = (true);
                this.operation = (transaction.operation);
                this.interest = (Math.ceil(transaction.getAmount() * 0.5) / 100.0);
        }
}
