package com.backend.services.myideapool.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.services.myideapool.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "select * from user where email = ?1", nativeQuery = true)
    User findUserByEmail(String email);

    @Query(value = "select * from user where refresh_token = ?1", nativeQuery = true)
    User findUserByRefreshToken(String refresh_token);

}