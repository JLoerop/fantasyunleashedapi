package com.joshua.fantasyunleashedapi.users.Services;

import com.joshua.fantasyunleashedapi.users.Model.User;
import com.joshua.fantasyunleashedapi.users.Repository.UserRepository;
import com.joshua.fantasyunleashedapi.users.Request.UserRequest;
import com.joshua.fantasyunleashedapi.utils.DuplicateUserException;
import com.joshua.fantasyunleashedapi.utils.PerformanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // service for registering a user using the JPA repository
    public User registerUser(UserRequest user){
        Instant startTime = PerformanceUtil.start();

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateUserException("Email already in use");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUserException("Username already in use");
        }

        User newUser = new User();

        try{
            newUser.setEmail(user.getEmail());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            userRepository.save(newUser);
        }
        catch(Exception e){
            System.out.println(e);
            throw new RuntimeException(e);
        }
        PerformanceUtil.stop(startTime);
        return newUser;
    }

    // service that will retrieve the user information using the JPA repository uses a try catch if the repository fails
    public User getUser(String email, String password){
        Instant startTime = PerformanceUtil.start();

        User user = null;

        try{
            user = userRepository.findByEmailAndPassword(email, password);
        }
        catch (Exception e){
            System.out.println(e);
            throw new RuntimeException(e);
        }

        PerformanceUtil.stop(startTime);
        return user;
    }
}
