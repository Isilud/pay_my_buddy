package com.paymybuddy.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    private Long accountId;

    @Column(name = "email")
    private String email;

    @Column(name = "number")
    private String number;

    @Column(name = "code")
    private String code;
}
