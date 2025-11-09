package com.sourcery.defect_registration_system.repository;

import com.sourcery.defect_registration_system.identity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
