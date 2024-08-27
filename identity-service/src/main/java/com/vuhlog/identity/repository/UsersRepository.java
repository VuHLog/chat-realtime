package com.vuhlog.identity.repository;

import com.vuhlog.identity.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Users> findByUsername(String username);

    Page<Users> findByUsernameContainingIgnoreCaseAndUsernameNot(String username, String myUserName, Pageable pageable);

    Page<Users> findByUsernameContainsIgnoreCase(String username, Pageable pageable);


}