package com.joshua.fantasyunleashedapi.users.Request;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


// request body to be able to register the user
public class UserRequest {
  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private String password;
}
