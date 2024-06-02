package com.programming.apiapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String jwtToken;
    private final Long id;
}
