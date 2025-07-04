package com.wolvie.fileShare.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wolvie.fileShare.model.Users;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);
}
