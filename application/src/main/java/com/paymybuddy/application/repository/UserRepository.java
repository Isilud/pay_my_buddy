package com.paymybuddy.application.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.paymybuddy.application.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
