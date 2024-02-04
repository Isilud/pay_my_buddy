package com.paymybuddy.application.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.application.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
