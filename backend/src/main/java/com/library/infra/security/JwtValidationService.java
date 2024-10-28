package com.library.infra.security;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Objects;

import static com.library.infra.security.JwtExtractDataService.extractExpiration;
import static com.library.infra.security.JwtExtractDataService.extractUserName;

@RequiredArgsConstructor
public class JwtValidationService {
    public static Boolean isValidToken(String token, String username) {
        String extractedUserName = extractUserName(token);
        return (Objects.equals(extractedUserName, username)) && !isTokenExpired(token);
    }

    public static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}