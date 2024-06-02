package com.programming.apiapplication.resource;

import com.programming.apiapplication.entity.AuthModel;
import com.programming.apiapplication.entity.JwtResponse;
import com.programming.apiapplication.entity.User;
import com.programming.apiapplication.entity.UserModel;
import com.programming.apiapplication.security.CustomUserDetailsService;
import com.programming.apiapplication.service.UserService;
import com.programming.apiapplication.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController()
public class AuthResource {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserModel userModel){
        return ResponseEntity.created(URI.create("/users/userID")).body(userService.createUser(userModel));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody AuthModel authModel) throws Exception {
        authenticate(authModel.getEmail(),authModel.getPassword());
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authModel.getEmail());
        User user = userService.findByEmail(authModel.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        //we need to generate the Jwt token
        return new ResponseEntity<JwtResponse>(new JwtResponse(token, user.getId()), HttpStatus.OK);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                    password));
        }catch (DisabledException e){
            throw new Exception("User Disabled");
        }catch (BadCredentialsException e){
            throw new Exception("Bad Credential");
        }
    }

}
