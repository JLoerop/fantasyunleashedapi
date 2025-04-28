package com.joshua.fantasyunleashedapi.users.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joshua.fantasyunleashedapi.users.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // custom jpa requests to check if the email or username exists already
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    // custom jpa request to find by email and password
    Optional<User> findByEmail(@Param("email") String email);

    User findByEmailAndPassword(@Param("email") String email, @RequestParam("password") String password);
}
