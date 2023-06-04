package com.example.springboottelegrambot.model;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    @Cacheable("user")
    Optional<User> findById(Long aLong);
}
