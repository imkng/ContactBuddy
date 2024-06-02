package com.programming.apiapplication.security;

import com.programming.apiapplication.entity.User;
import com.programming.apiapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User exsitingUser = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User is not present with emailId: " + email));

        return new org.springframework.security.core.userdetails.User(exsitingUser.getEmail(),
                exsitingUser.getPassword(), new ArrayList<>());
    }
}
