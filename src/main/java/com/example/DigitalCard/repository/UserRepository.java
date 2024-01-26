package com.example.DigitalCard.repository;

import com.example.DigitalCard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPassword(String password);
    User findByEmailAndPassword(String email, String password);
}
