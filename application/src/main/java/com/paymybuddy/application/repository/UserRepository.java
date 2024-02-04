package com.paymybuddy.application.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.application.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
