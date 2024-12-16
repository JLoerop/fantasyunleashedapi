package com.joshua.fantasyunleashedapi.users.Services;

import com.joshua.fantasyunleashedapi.users.Model.User;
import com.joshua.fantasyunleashedapi.users.Repository.UserRepository;
import com.joshua.fantasyunleashedapi.utils.PerformanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
