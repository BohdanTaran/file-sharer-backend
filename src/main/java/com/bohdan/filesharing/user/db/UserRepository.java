package com.bohdan.filesharing.user.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by email
     *
     * @param email user's email
     * @return Optional<User> - user or empty Optional
     */
    Optional<User> findByEmail(String email);
    /**
     * Check user existence by email
     *
     * @param email user's email
     * @return true if user exists, false if not
     */
    boolean existsByEmail(String email);
}
