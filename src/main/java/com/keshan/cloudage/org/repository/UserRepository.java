package com.keshan.cloudage.org.repository;

import com.keshan.cloudage.org.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
