package com.joshua.fantasyunleashedapi.users.Controller;

import com.joshua.fantasyunleashedapi.users.Model.User;
import com.joshua.fantasyunleashedapi.users.Request.UserRequest;
import com.joshua.fantasyunleashedapi.users.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    // request to be able to login the user
    @GetMapping("/login")
    public User login(@RequestParam("email") String email, @RequestParam("password") String password){
        return userService.getUser(email, password);
    }

    // request to be able to register a user
    @PostMapping("/register")
    public User register(@RequestBody UserRequest user){
        return userService.registerUser(user);
    }
}
