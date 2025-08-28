package com.keshan.cloudage.org.repository;

import com.keshan.cloudage.org.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<User>findByEmailIgnoreCase(String email);

}
