package com.paymybuddy.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private String date;

    @Column(name = "interest")
    private Integer interest;

    @Column(name = "friend_name")
    private String friend_name;
}