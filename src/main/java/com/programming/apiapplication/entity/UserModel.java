package com.programming.apiapplication.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @NotBlank(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Email should not be empty")
    @Email(message = "Enter Valid email")
    private String email;
    @NotNull(message = "Password should not be empty")
    @Size(min = 8, message = "Password should be atleast 8 char long")
    private String password;
}
