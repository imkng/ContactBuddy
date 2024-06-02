package com.programming.apiapplication.service;

import com.programming.apiapplication.entity.User;
import com.programming.apiapplication.entity.UserModel;
import com.programming.apiapplication.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserModel userModel){
        User user = new User();
        BeanUtils.copyProperties(userModel, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User Not found for the email: " + email)
        );
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found for this email: " + email));
    }


    public User readUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found for id: " + userId));
    }


    public User updateUser(UserModel userModel, Long userId) {
        User existingUser = readUser(userId);
        existingUser.setName(userModel.getName()!= null ? userModel.getName() : existingUser.getName());
        existingUser.setEmail(userModel.getEmail()!= null ? userModel.getEmail() : existingUser.getEmail());
        existingUser.setPassword(userModel.getPassword()!= null ? userModel.getPassword() : existingUser.getPassword());
        return userRepository.save(existingUser);
    }
}
