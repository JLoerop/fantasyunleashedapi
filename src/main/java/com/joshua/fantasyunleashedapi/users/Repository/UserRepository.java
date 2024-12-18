package com.joshua.fantasyunleashedapi.users.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joshua.fantasyunleashedapi.users.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    // custom jpa request to find by email and password
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
